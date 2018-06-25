package com.jbb.server.mq;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.activemq.artemis.api.core.client.ClientSession;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.ShutdownException;

/**
 * Use common pools library for MQ producers and consumers pooling
 * <br/>
 * See <a href="https://commons.apache.org/proper/commons-pool/api-2.4.2/org/apache/commons/pool2/impl/GenericObjectPool.html">GenericObjectPool</a>
 * <br/><br/>
 * 
 * A GenericObjectPool provides a number of configurable parameters:
 * <ul>
 *   <li>maxActive controls the maximum number of objects that can be allocated by the pool 
 *     (checked out to clients, or idle awaiting checkout) at a given time. 
 *     When non-positive, there is no limit to the number of objects that can be managed by the pool at one time. 
 *     When maxActive is reached, the pool is said to be exhausted. 
 *     The default setting for this parameter is 8.</li>
 *   <li>maxIdle controls the maximum number of objects that can sit idle in the pool at any time. 
 *     When negative, there is no limit to the number of objects that may be idle at one time. 
 *     The default setting for this parameter is 8.</li>
 *   <li>whenExhaustedAction specifies the behavior of the borrowObject() method when the pool is exhausted:
 *     <ul>
 *       <li>When whenExhaustedAction is WHEN_EXHAUSTED_FAIL, borrowObject() will throw a NoSuchElementException</li>
 *       <li>When whenExhaustedAction is WHEN_EXHAUSTED_GROW, borrowObject() will create a new object and return it 
 *         (essentially making maxActive meaningless.)</li>
 *       <li>When whenExhaustedAction is WHEN_EXHAUSTED_BLOCK, borrowObject() will block (invoke Object.wait()) 
 *         until a new or idle object is available. If a positive maxWait value is supplied, 
 *         then borrowObject() will block for at most that many milliseconds, after which a NoSuchElementException will be thrown. 
 *         If maxWait is non-positive, the borrowObject() method will block indefinitely.</li>
 *     </ul>
 *     The default whenExhaustedAction setting is WHEN_EXHAUSTED_BLOCK and the default maxWait setting is -1. 
 *     By default, therefore, borrowObject will block indefinitely until an idle instance becomes available.
 *   </li>
 *   <li>When testOnBorrow is set, the pool will attempt to validate each object before it is returned from the borrowObject() method. 
 *     (Using the provided factory's PoolableObjectFactory.validateObject(java.lang.Object) method.) 
 *     Objects that fail to validate will be dropped from the pool, and a different object will be borrowed. 
 *     The default setting for this parameter is false.</li>
 *   <li>When testOnReturn is set, the pool will attempt to validate each object before it is returned to the pool 
 *     in the returnObject(java.lang.Object) method. 
 *     (Using the provided factory's PoolableObjectFactory.validateObject(java.lang.Object) method.) 
 *     Objects that fail to validate will be dropped from the pool. The default setting for this parameter is false.</li>
 * </ul>
 */
class PoolsFactory {
    private static final ConcurrentHashMap<String, ObjectPool<Producer>> producerPools = new ConcurrentHashMap<>();
    private static final ObjectPool<ClientSession> sessionsPool = new GenericObjectPool<>(new PoolableSessionFactory(), createGenericConfig());
    private static volatile boolean down = false;

    private static GenericObjectPoolConfig createGenericConfig() {
        // set pool parameters
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(Integer.MAX_VALUE);
        config.setMaxIdle(PropertyManager.getIntProperty("jbb.mq.pool.maxIdle", 10));
        config.setBlockWhenExhausted(true);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        config.setTestWhileIdle(false);

        return config;
    }

    /**
     * Create a new or return an existing message producers pool by an address
     * @return message producers pool
     */
    static ObjectPool<Producer> getProducersPool(String address) {
        checkDown();
        return producerPools.computeIfAbsent(address, k -> new GenericObjectPool<>(
                new PoolableProducerFactory(address), createGenericConfig()));
    }

    static ClientSession getSession() throws Exception {
        checkDown();
        return sessionsPool.borrowObject();
    }

    static void returnSession(ClientSession session) throws Exception {
        if (session == null) return;

        if (down) {
            if (!session.isClosed()) session.close();
        } else {
            sessionsPool.returnObject(session);
        }
    }

    private static void checkDown() {
        if (down) throw new ShutdownException("MQ pools factory down");
    }

    static void destroyPool(ObjectPool<?> pool) {
        if (pool == null) return;

        try {
            pool.clear();
        } catch (Exception ignore) {
        }

        try {
            pool.close();
        } catch (Exception ignore) {
        }
    }

    /**
     * Clean the factory on destroy
     */
    static synchronized void shutdown() {
        if (down) return;
        down = true;

        for (ObjectPool<Producer> pool : producerPools.values()) {
            destroyPool(pool);
        }

        producerPools.clear();

        destroyPool(sessionsPool);
    }
}
