package com.jbb.mgt.core.domain;

public class Money {
    private int loanMoney; // 放款金额
    private int returnMoney; // 回款金额
    private int dueMoney; // 累计待收金额
    private int dueToMoney; // 到期金额

    public int getDueToMoney() {
        return dueToMoney;
    }

    public void setDueToMoney(int dueToMoney) {
        this.dueToMoney = dueToMoney;
    }

    public int getLoanMoney() {
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