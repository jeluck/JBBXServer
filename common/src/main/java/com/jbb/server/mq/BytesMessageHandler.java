package com.jbb.server.mq;

import org.apache.activemq.artemis.api.core.ActiveMQBuffer;
import org.apache.activemq.artemis.api.core.client.ClientMessage;
import org.apache.activemq.artemis.api.core.client.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BytesMessageHandler implements MessageHandler {
    private static Logger logger = LoggerFactory.getLogger(BytesMessageHandler.class);

    private final MessageListener listener;
    private final String listenerName;
    private volatile long taskCount;
    private volatile boolean running;

    BytesMessageHandler(MessageListener listener) {
        this.listener = listener;
        this.listenerName = listener.getClass().getName();
    }

    @Override
    public void onMessage(ClientMessage message) {
        if (message == null) return;

        try {
            running = true;
            ActiveMQBuffer buf = message.getBodyBuffer();
            byte[] messageBody = new byte[buf.readableBytes()];
            buf.readBytes(messageBody);

            message.individualAcknowledge();

            listener.onMessage(messageBody);
        } catch (Exception e) {
            logger.error("Exception in handling a message by " + listener.getClass().getName(), e);
        } finally {
            running = false;
            taskCount++;
        }
    }

    String getListenerName() {
        return listenerName;
    }

    long getTaskCount() {
        return taskCount;
    }

    boolean isRunning() {
        return running;
    }
}
