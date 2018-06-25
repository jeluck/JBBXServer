package com.jbb.server.mq;

import org.apache.activemq.artemis.api.core.client.FailoverEventListener;
import org.apache.activemq.artemis.api.core.client.FailoverEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FailoverListener implements FailoverEventListener {
    private static Logger logger = LoggerFactory.getLogger(FailoverListener.class);

    @Override
    public void failoverEvent(FailoverEventType event) {
        logger.error("MQ failover: " + event);
    }
}
