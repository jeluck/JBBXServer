package com.jbb.server.mq;

import java.util.ArrayList;

public class ClosableConsumer {
    private ArrayList<Consumer> consumers;

    void addConsumer(Consumer consumer) {
        if (consumers == null) consumers = new ArrayList<>();
        consumers.add(consumer);
    }

    public void close() {
        ArrayList<Consumer> consumersCopy = consumers;
        consumers = null;

        if (consumersCopy != null) {
            for (Consumer consumer : consumersCopy) {
                consumer.close();
                MqClient.removeConsumer(consumer);
            }
        }
    }
}
