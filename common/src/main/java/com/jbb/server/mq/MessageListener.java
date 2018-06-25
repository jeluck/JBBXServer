package com.jbb.server.mq;

public interface MessageListener extends AbstractListener {
    void onMessage(byte[] messageBody);
}
