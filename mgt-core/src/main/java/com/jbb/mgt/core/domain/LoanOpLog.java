package com.jbb.mgt.core.domain;

import com.jbb.server.common.util.DateUtil;

import java.sql.Timestamp;

public class LoanOpLog {
    private int logId; // 日志编号
    private int loanId; // 操作账户ID
    private int opType; // 操作类型
    private Timestamp opDate; // 操作日期
    private String opReason; // 操作理由
    private String opComment; // 操作记录
    private int accountId;

    private Account account;

    public LoanOpLog() {
        super();
    }

    public LoanOpLog(int loanId, int opType, int accountId, String opComment, String opReason) {
        super();
        this.loanId = loanId;
        this.opType = opType;
        this.opDate = DateUtil.getCurrentTimeStamp();
        this.opReason = opReason;
        this.opComment = opComment;
        this.accountId = accountId;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
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

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "AccountOpLog [logId=" + logId + ", loanId=" + loanId + ", opType=" + opType + ", opDate=" + opDate
            + ", opReason=" + opReason + ", opComment=" + opComment + ", accountId=" + accountId + ", account="
            + account + "]";
    }

}
