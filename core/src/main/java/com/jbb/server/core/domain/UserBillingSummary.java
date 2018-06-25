package com.jbb.server.core.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBillingSummary {

    private double totalBorrow;
    private double totalBalance;
    private double totalRepayment;
    private double totalInterest;

    private List<Billing> recentBillings;

    private Billing overdueBilling;
    private Billing recentBilling;

    public UserBillingSummary() {

    }

    public UserBillingSummary(double totalBorrow, double totalBalance, double totalRepayment, double totalInterest,
        Billing overdueBilling, Billing recentBilling) {
        super();
        this.totalBorrow = totalBorrow;
        this.totalBalance = totalBalance;
        this.totalRepayment = totalRepayment;
        this.totalInterest = totalInterest;
        this.overdueBilling = overdueBilling;
        this.recentBilling = recentBilling;
    }

    public double getTotalBorrow() {
        return totalBorrow;
    }

    public void setTotalBorrow(double totalBorrow) {
        this.totalBorrow = totalBorrow;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public double getTotalRepayment() {
        return totalRepayment;
    }

    public void setTotalRepayment(double totalRepayment) {
        this.totalRepayment = totalRepayment;
    }

    public double getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(double totalInterest) {
        this.totalInterest = totalInterest;
    }

    public Billing getOverdueBilling() {
        return overdueBilling;
    }

    public void setOverdueBilling(Billing overdueBilling) {
        this.overdueBilling = overdueBilling;
    }

    public Billing getRecentBilling() {
        return recentBilling;
    }

    public void setRecentBilling(Billing recentBilling) {
        this.recentBilling = recentBilling;
    }

    public List<Billing> getRecentBillings() {
        return recentBillings;
    }

    public void setRecentBillings(List<Billing> recentBillings) {
        this.recentBillings = recentBillings;
    }

    @Override
    public String toString() {
        return "UserBillingSummary [totalBorrow=" + totalBorrow + ", totalBalance=" + totalBalance + ", totalRepayment="
            + totalRepayment + ", totalInterest=" + totalInterest + ", recentBillings=" + recentBillings
            + ", overdueBilling=" + overdueBilling + ", recentBilling=" + recentBilling + "]";
    }

}
