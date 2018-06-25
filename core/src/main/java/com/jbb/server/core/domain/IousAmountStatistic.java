package com.jbb.server.core.domain;

public class IousAmountStatistic {
    // 总金额
    private double amount;
    // 总利息
    private double interest;
    // 笔数
    private int cnt;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    @Override
    public String toString() {
        return "IousAmountStatistic [amount=" + amount + ", interest=" + interest + ", cnt=" + cnt + "]";
    }
    
    

}
