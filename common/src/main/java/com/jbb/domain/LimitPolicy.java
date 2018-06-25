package com.jbb.domain;

public class LimitPolicy {
    int userId;
    int limit;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "LimitPolicy [userId=" + userId + ", limit=" + limit + "]";
    }

}
