package com.jbb.server.mq;

import org.apache.activemq.artemis.api.core.client.ClientSession;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.common.exception.ExecutionException;

/**
 * Poolable message consumer factory
 */
class PoolableSessionFactory implements PooledObjectFactory<ClientSession> {
    private static Logger logger = LoggerFactory.getLogger(PoolableSessionFactory.class);

    /**
     * Create MQ session and consumer
     */
    @Override
    public PooledObject<ClientSession> makeObject() throws Exception {
        try {
            return new DefaultPooledObject<>(SessionFactory.createSession());
        } catch (Exception e) {
            throw new ExecutionException("Exception in creating MQ session", e);
        }
    }

    /**
     * Close session and consumer
     */
    @Override
    public void destroyObject(PooledObject<ClientSession> object) throws Exception {
        if (object == null) return;
        ClientSession session = object.getObject();
        if (session == null) return;

        try {
            session.close();
        } catch (Exception e) {
            logger.warn("Exception in destroying session: " + e.toString());
        }
    }

    @Override
    public boolean validateObject(PooledObject<ClientSession> object) {
        return object != null && object.getObject() != null;
    }

    @Override
    public void activateObject(PooledObject<ClientSession> object) throws Exception {
        // do nothing
    }

    @Override
    public void passivateObject(PooledObject<ClientSession> object) throws Exception {
        // do nothing
    }
}
