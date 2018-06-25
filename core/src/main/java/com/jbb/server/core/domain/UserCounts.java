package com.jbb.server.core.domain;

public class UserCounts {

    private String statisDate;
    private int type;
    private int count;

    public String getStatisDate() {
        return statisDate;
    }

    public void setStatisDate(String statisDate) {
        this.statisDate = statisDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "UserCounts [statisDate=" + statisDate + ", type=" + type + ", count=" + count + "]";
    }

}
