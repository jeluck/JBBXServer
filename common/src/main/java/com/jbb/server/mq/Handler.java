package com.jbb.server.mq;

import org.apache.activemq.artemis.api.core.client.MessageHandler;

class Handler {
    private MessageHandler messageHandler;
    private String queue;
    private String address;
    private int consumersCount;

    Handler(MessageHandler messageHandler, String queue, String address, int consumersCount) {
        this.messageHandler = messageHandler;
        this.queue = queue;
        this.address = address;
        this.consumersCount = consumersCount;
    }

    MessageHandler getMessageHandler() {
        return messageHandler;
    }

    String getQueue() {
        return queue;
    }

    int getConsumersCount() {
        return consumersCount;
    }

    public String getAddress() {
        return address;
    }
}
