package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

import com.jbb.server.common.util.DateUtil;

public class AccountOpLog {
    private int logId; // 日志编号
    private int applyId; // 用户申请ID
    private int opType; // 操作类型
    private Timestamp opDate; // 操作日期
    private String opReason; // 操作理由
    private String opComment; // 操作记录
    private int accountId;

    private Account account;

    public AccountOpLog() {
        super();
    }

    public AccountOpLog(int applyId, int opType, int accountId, String opComment, String opReason) {
        super();
        this.applyId = applyId;
        this.opType = opType;
        this.opDate = DateUtil.getCurrentTimeStamp();
        this.opReason = opReason;
        this.opComment = opComment;
        this.accountId = accountId;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public Integer getOpType() {
        return opType;
    }

    public void setOpType(Integer opType) {
        this.opType = opType;
    }

    public Timestamp getOpDate() {
        return opDate;
    }

    public void setOpDate(Timestamp opDate) {
        this.opDate = opDate;
    }

    public String getOpReason() {
        return opReason;
    }

    public void setOpReason(String opReason) {
        this.opReason = opReason;
    }

    public String getOpComment() {
        return opComment;
    }

    public void setOpComment(String opComment) {
        this.opComment = opComment;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public void setApplyId(int applyId) {
        this.applyId = applyId;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "AccountOpLog [logId=" + logId + ", applyId=" + applyId + ", opType=" + opType + ", opDate=" + opDate
            + ", opReason=" + opReason + ", opComment=" + opComment + ", accountId=" + accountId + ", account="
            + account + "]";
    }

}
