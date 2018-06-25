package com.jbb.server.mq;

import org.apache.activemq.artemis.api.core.client.ClientConsumer;
import org.apache.activemq.artemis.api.core.client.ClientSession;

/**
 * Message consumer and client session pair
 */
class Consumer {
    private ClientSession session;
    private ClientConsumer consumer;

    Consumer(ClientSession session, ClientConsumer consumer) {
        this.session = session;
        this.consumer = consumer;
    }

    ClientConsumer getConsumer() {
        return consumer;
    }

    void close() {
        try {
            ClientConsumer consumerCopy = consumer;
            consumer = null;
            if (consumerCopy != null) consumerCopy.close();
        } catch (Exception ignore) {
        }

        try {
            ClientSession sessionCopy = session;
            session = null;
            if (sessionCopy != null) sessionCopy.close();
        } catch (Exception ignore) {
        }
    }
}
