package com.jbb.server.core.domain;

import java.sql.Timestamp;

public class IouOperationRecord {
    private String iouCode;
    private int fromUser;
    private int toUser;
    private String opName;
    private Timestamp opDate;
    private String params;

    public IouOperationRecord() {}

    public IouOperationRecord(String iouCode, int fromUser, int toUser, String opName, String params) {
        this.iouCode = iouCode;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.opName = opName;
        this.params = params;
    }

    public String getIouCode() {
        return iouCode;
    }

    public void setIouCode(String iouCode) {
        this.iouCode = iouCode;
    }

    public int getFromUser() {
        return fromUser;
    }

    public void setFromUser(int fromUser) {
        this.fromUser = fromUser;
    }

    public int getToUser() {
        return toUser;
    }

    public void setToUser(int toUser) {
        this.toUser = toUser;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public Timestamp getOpDate() {
        return opDate;
    }

    public void setOpDate(Timestamp opDate) {
        this.opDate = opDate;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "IouOperationRecord [iouCode=" + iouCode + ", fromUser=" + fromUser + ", toUser=" + toUser + ", opName="
            + opName + ", opDate=" + opDate + ", params=" + params + "]";
    }
}
