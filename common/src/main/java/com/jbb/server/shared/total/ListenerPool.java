package com.jbb.server.shared.total;

public class ListenerPool extends Listener {
    private int maximumPoolSize;
    private int poolSize;
    private int poolQueueSize;

    public ListenerPool() {
    }
    
    public ListenerPool(String className, int activeCount, int maximumPoolSize,
                        int poolSize, long taskCount, int poolQueueSize, int type)
    {
        super(className, taskCount, activeCount, type);
        this.maximumPoolSize = maximumPoolSize;
        this.poolQueueSize = poolQueueSize;
        this.poolSize = poolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public int getPoolQueueSize() {
        return poolQueueSize;
    }

    public void setPoolQueueSize(int poolQueueSize) {
        this.poolQueueSize = poolQueueSize;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
}
