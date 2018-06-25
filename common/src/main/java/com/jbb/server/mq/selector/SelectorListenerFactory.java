package com.jbb.server.mq.selector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.common.Home;
import com.jbb.server.mq.MqClient;

public class SelectorListenerFactory<T extends SelectorResponse> {
    private static final Logger logger = LoggerFactory.getLogger(SelectorListenerFactory.class);

    private volatile SelectorMessageListener<T> listener;
    private volatile String queue;

    public synchronized void registerListener(String name, String queuePrefix, int handlersNum, Class<T> objectType) {
        if (listener != null) return;

        try {
            if (name == null) name = Home.convenientModuleName();
            queue = queuePrefix + name;

            SelectorMessageListener<T> newListener = new SelectorMessageListener<>(objectType);
            MqClient.setMessageListener(queue, queue, false, newListener, handlersNum);

            this.listener = newListener;
        } catch (Exception e) {
            logger.error("Exception in starting SelectorListener for " + objectType.getName() + " on queue " + queue + ", handlersNum=" + handlersNum, e);
        }
    }

    public SelectorMessageCollector<T> getCollector() {
        return listener;
    }

    public String getQueue() {
        return queue;
    }
}
