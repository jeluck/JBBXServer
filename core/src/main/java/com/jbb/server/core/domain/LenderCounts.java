package com.jbb.server.core.domain;

import java.util.List;

public class LenderCounts {

    private int userId;
    private List<UserCounts> dateCounts;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<UserCounts> getDateCounts() {
        return dateCounts;
    }

    public void setDateCounts(List<UserCounts> dateCounts) {
        this.dateCounts = dateCounts;
    }

    @Override
    public String toString() {
        return "LenderCounts [userId=" + userId + ", dateCounts=" + dateCounts + "]";
    }

}
