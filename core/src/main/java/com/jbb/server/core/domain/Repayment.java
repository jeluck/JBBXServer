package com.jbb.server.core.domain;

import java.sql.Timestamp;

public class Repayment {

    private int repaymentId;
    private int billingDetailId;
    private double amount;
    private Timestamp repayDate;

    public int getRepaymentId() {
        return repaymentId;
    }

    public void setRepaymentId(int repaymentId) {
        this.repaymentId = repaymentId;
    }

    public int getBillingDetailId() {
        return billingDetailId;
    }

    public void setBillingDetailId(int billingDetailId) {
        this.billingDetailId = billingDetailId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Timestamp repayDate) {
        this.repayDate = repayDate;
    }

    @Override
    public String toString() {
        return "Repayment [repaymentId=" + repaymentId + ", billingDetailId=" + billingDetailId + ", amount=" + amount
            + ", repayDate=" + repayDate + "]";
    }

}
