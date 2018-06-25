package com.jbb.server.shared.total;

public class Listener {
    public static final int TYPE_MESSAGE = 1;
    public static final int TYPE_SYSTEM = 2;

    private String queue;
    private String address;
    private String className;
    private long taskCount;
    private long queueSize;
    private int activeCount;
    private int type;
    private int consumersCount;

    public Listener() {
    }

    public Listener(String queue, String address, String className, long taskCount, int consumersCount, int activeCount, int type) {
        this.queue = queue;
        this.address = address;
        this.className = className;
        this.taskCount = taskCount;
        this.consumersCount = consumersCount;
        this.activeCount = activeCount;
        this.type = type;
    }

    public Listener(String className, long taskCount, int activeCount, int type) {
        this.className = className;
        this.taskCount = taskCount;
        this.activeCount = activeCount;
        this.type = type;
    }

    public void increment(long taskCount, int activeCount, int consumersCount) {
        this.taskCount += taskCount;
        this.activeCount += activeCount;
        this.consumersCount += consumersCount;
    }

    public String getQueue() {
        return queue;
    }
    
    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(long taskCount) {
        this.taskCount = taskCount;
    }

    public long getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(long queueSize) {
        this.queueSize = queueSize;
    }

    public int getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getConsumersCount() {
        return consumersCount;
    }

    public void setConsumersCount(int consumersCount) {
        this.consumersCount = consumersCount;
    }
}
