package com.jbb.mgt.core.domain;

public class Statistics {
    private int entryNum; // 进件数
    private int auditingNum; // 审核数
    private int loanNum; // 放款数
    private int initNum; // 初审数
    private int friendsNum; // 好友数

    public int getInitNum() {
        return initNum;
    }

    public void setInitNum(int initNum) {
        this.initNum = initNum;
    }

    public int getFriendsNum() {
        return friendsNum;
    }

    public void setFriendsNum(int friendsNum) {
        this.friendsNum = friendsNum;
    }

    public int getEntryNum() {
        return entryNum;
    }

    public void setEntryNum(int entryNum) {
        this.entryNum = entryNum;
    }

    public int getAuditingNum() {
        return auditingNum;
    }

    public void setAuditingNum(int auditingNum) {
        this.auditingNum = auditingNum;
    }

    public int getLoanNum() {
        return loanNum;
    }

    public void setLoanNum(int loanNum) {
        this.loanNum = loanNum;
    }

}
