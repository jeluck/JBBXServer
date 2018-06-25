package com.jbb.mgt.core.domain;

public class StatisticsMoney {
    private int entryNum; // 进件数
    private int auditingNum; // 审核数
    private int loanNum; // 放款数
    private int initNum; // 初审数
    private int friendsNum; // 好友数
    private int loanMoney; // 放款金额
    private int returnMoney; // 回款金额
    private int dueMoney; // 累计待收金额
    private int dueToMoney; // 到期金额

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

	public int getDueToMoney() {
		return dueToMoney;
	}

	public void setDueToMoney(int dueToMoney) {
		this.dueToMoney = dueToMoney;
	}

	public int getBorrowingMoney() {
		return borrowingMoney;
	}

	public void setBorrowingMoney(int borrowingMoney) {
		this.borrowingMoney = borrowingMoney;
	}

	private int borrowingMoney;// 借条额

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

    public double getLoanMoney() {
        return loanMoney;
    }

    public void setLoanMoney(int loanMoney) {
        this.loanMoney = loanMoney;
    }

    public int getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(int returnMoney) {
        this.returnMoney = returnMoney;
    }

    public int getDueMoney() {
        return dueMoney;
    }

    public void setDueMoney(int dueMoney) {
        this.dueMoney = dueMoney;
    }

}
