package com.jbb.server.mq;

import org.apache.activemq.artemis.api.core.client.ClientProducer;
import org.apache.activemq.artemis.api.core.client.ClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Poolable message producer
 */
class Producer {
    private static Logger logger = LoggerFactory.getLogger(Producer.class);

    private ClientSession session;
    private ClientProducer producer;
    
    Producer(ClientSession session, ClientProducer producer) {
        this.session = session;
        this.producer = producer;
    }

    ClientSession getSession() {
        return session;
    }

    ClientProducer getProducer() {
        return producer;
    }

    void destroy() {
        try {
            if (producer != null) {
                producer.close();
                producer = null;
            }
        } catch (Exception e) {
            logger.warn("Exception in destroying producer: " + e.toString());
        }

        try {
            if (session != null) {
                session.close();
                session = null;
            }
        } catch (Exception e) {
            logger.warn("Exception in destroying session: " + e.toString());
        }
    }
}
