package com.jbb.mgt.core.domain;

/**
 * mgt->jbb打借条实体类
 *
 * @author wyq
 * @date 2018/5/31 09:22
 */
public class ReMgtIou {
    private double borrowingAmount;
    private String borrowingDate;
    private String repaymentDate;
    private double annualRate;
    private int borrowerId;
    private int lenderId;

    public double getBorrowingAmount() {
        return borrowingAmount;
    }

    public void setBorrowingAmount(double borrowingAmount) {
        this.borrowingAmount = borrowingAmount;
    }

    public String getBorrowingDate() {
        return borrowingDate;
    }

    public void setBorrowingDate(String borrowingDate) {
        this.borrowingDate = borrowingDate;
    }

    public String getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(String repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public double getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(double annualRate) {
        this.annualRate = annualRate;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public int getLenderId() {
        return lenderId;
    }

    public void setLenderId(int lenderId) {
        this.lenderId = lenderId;
    }

    public ReMgtIou() {
        super();
    }

    public ReMgtIou(double borrowingAmount, String borrowingDate, String repaymentDate, double annualRate,
        int borrowerId, int lenderId) {
        this.borrowingAmount = borrowingAmount;
        this.borrowingDate = borrowingDate;
        this.repaymentDate = repaymentDate;
        this.annualRate = annualRate;
        this.borrowerId = borrowerId;
        this.lenderId = lenderId;
    }
}
