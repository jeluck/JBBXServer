package com.jbb.server.common;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.PropertyDefiner;
import ch.qos.logback.core.status.Status;

/**
 * Utility class to get home directory path by Logback
 * @author Vincent Tang
 */
public class HomeProperty implements PropertyDefiner {
    private Context context;

    @Override
    public String getPropertyValue() {
        return Home.getHomeDir();
    }

    @Override
    public void addError(String arg0) {
    }

    @Override
    public void addError(String arg0, Throwable arg1) {
    }

    @Override
    public void addInfo(String arg0) {
    }

    @Override
    public void addInfo(String arg0, Throwable arg1) {
    }

    @Override
    public void addStatus(Status arg0) {
    }

    @Override
    public void addWarn(String arg0) {
    }

    @Override
    public void addWarn(String arg0, Throwable arg1) {
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }
}
