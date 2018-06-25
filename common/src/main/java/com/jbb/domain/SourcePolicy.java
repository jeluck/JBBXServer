package com.jbb.domain;

public class SourcePolicy {
    String sourceId;
    int userId;
    double p;
    public String getSourceId() {
        return sourceId;
    }
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public double getP() {
        return p;
    }
    public void setP(double p) {
        this.p = p;
    }
    @Override
    public String toString() {
        return "SourcePolicy [sourceId=" + sourceId + ", userId=" + userId + ", p=" + p + "]";
    }

    

}
