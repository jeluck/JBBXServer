package com.jbb.server.common.concurrent;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.jbb.server.common.exception.ExecutionException;

/**
 * Unlimited objects pool based on Apache commons pool2.<br/>
 * Borrowed object will be destroyed after returning, if the number of idle objects is more than the maxIdle parameter.
 * It should be used for pooling for pooling objects with expensive resources.<br/>
 * It provides two methods to borrow an object from the pool or create a new one, if the pool is empty,
 * and to return the object to the pool.
 * Class T must have a default constructor or a factory has to be provided.<br/>
 * Typical usage is next:<br/>
 * <code>
 * <br/>  try {
 * <br/>      obj = pool.getObject();
 * <br/>      ...
 * <br/>  } finally {
 * <br/>      pool.returnObject(obj);
 * <br/>  }
 * </code>
 * <br/><br/>
 * The call() method wraps this sequence.<br/>
 *
 * @author VincentTang
 */
public class CommonObjectPool<T> implements ObjectPool<T> {
    /** Objects pool */
    private final GenericObjectPool<T> pool;

    public CommonObjectPool(Class<T> clazz, int maxIdle) {
        pool = new GenericObjectPool<>(new PoolableObjectFactory<>(clazz), createGenericConfig(maxIdle));
    }

    public CommonObjectPool(ObjectFactory<T> factory, int maxIdle) {
        pool = new GenericObjectPool<>(new PoolableObjectFactory<>(factory), createGenericConfig(maxIdle));
    }

    /**
     * Borrow object from the pool or create a new one
     * @return object from the pool
     */
    public T getObject() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            throw new ExecutionException(e);
        }
    }
    
    /**
     * Return the object to the pool
     * @param obj object
     */
    public void returnObject(T obj) {
        try {
            pool.returnObject(obj);
        } catch (Exception ignore) {
        }
    }

    /**
     * @return the pool size
     */
    @Override
    public int poolSize() {
        return pool.getNumActive() + pool.getNumIdle();
    }

    static class PoolableObjectFactory<T> implements PooledObjectFactory<T> {
        private ObjectFactory<T> factory;

        private PoolableObjectFactory(Class<T> clazz) {
            this.factory = () -> {
                try {
                    return clazz.newInstance();
                } catch (Exception e) {
                    throw new ExecutionException(e);
                }
            };
        }

        private PoolableObjectFactory(ObjectFactory<T> factory) {
            this.factory = factory;
        }

        @Override
        public PooledObject<T> makeObject() throws Exception {
            return new DefaultPooledObject<>(factory.create());
        }

        @Override
        public void destroyObject(PooledObject<T> object) throws Exception {
            // do nothing
        }

        @Override
        public boolean validateObject(PooledObject<T> poolableConsumer) {
            // do nothing
            return true;
        }

        @Override
        public void activateObject(PooledObject<T> poolableConsumer) throws Exception {
            // do nothing
        }

        @Override
        public void passivateObject(PooledObject<T> poolableConsumer) throws Exception {
            // do nothing
        }
    }

    private GenericObjectPoolConfig createGenericConfig(int maxIdle) {
        // set pool parameters
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(Integer.MAX_VALUE);
        config.setMaxIdle(maxIdle);
        config.setBlockWhenExhausted(true);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        config.setTestWhileIdle(false);

        return config;
    }
}
