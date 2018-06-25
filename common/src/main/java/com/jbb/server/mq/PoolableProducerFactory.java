package com.jbb.server.mq;

import org.apache.activemq.artemis.api.core.client.ClientProducer;
import org.apache.activemq.artemis.api.core.client.ClientSession;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.common.exception.ExecutionException;

/**
 * Poolable message producer factory
 */
class PoolableProducerFactory implements PooledObjectFactory<Producer> {
    private static Logger logger = LoggerFactory.getLogger(PoolableProducerFactory.class);

    private final String address;

    PoolableProducerFactory(String address) {
        this.address = address;
    }

    /**
     * Create MQ session and producer
     */
    @Override
    public PooledObject<Producer> makeObject() throws Exception {
        ClientProducer producer = null;
        ClientSession session = null;

        try {
            session = SessionFactory.createSession();
            producer = session.createProducer(address);
            return new DefaultPooledObject<>(new Producer(session, producer));
        } catch (Exception e) {
            logger.error("Exception in creating MQ session", e);

            try {
                if (producer != null) producer.close();
            } catch (Exception ee) {
                logger.warn("Exception in closing producer: " + ee.toString());
            }

            try {
                if (session != null) session.close();
            } catch (Exception ee) {
                logger.warn("Exception in closing session: " + ee.toString());
            }

            throw new ExecutionException("Exception in creating MQ producer", e);
        }
    }

    /**
     * Close session and producer
     */
    @Override
    public void destroyObject(PooledObject<Producer> object) throws Exception {
        if (object == null) return;
        Producer producer = object.getObject();
        if (producer != null) producer.destroy();
    }

    @Override
    public boolean validateObject(PooledObject<Producer> object) {
        return object != null && object.getObject() != null;
    }

    @Override
    public void activateObject(PooledObject<Producer> object) throws Exception {
        // do nothing
    }

    @Override
    public void passivateObject(PooledObject<Producer> object) throws Exception {
        // do nothing
    }
}
