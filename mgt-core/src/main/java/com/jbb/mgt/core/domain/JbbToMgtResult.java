package com.jbb.mgt.core.domain;

public class JbbToMgtResult {
    private Integer status;
    private int opType;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public JbbToMgtResult(Integer status, int opType) {
        this.status = status;
        this.opType = opType;
    }
}
