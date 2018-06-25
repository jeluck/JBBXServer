package com.jbb.server.mq;

public interface ObjectListener extends AbstractListener {
    void onObject(Object object);
}
