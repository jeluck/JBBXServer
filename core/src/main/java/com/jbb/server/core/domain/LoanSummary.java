package com.jbb.server.core.domain;

public class LoanSummary {
    /**
     * 借款总金额
     */
    private double totalBalance;
    /**
     * 需偿还总金额
     */
    private double totalPayment;
    /**
     * 已还总金额
     */

    private double totalPaid;
    /**
     * 最近一条待还款记录
     */
    private LoanRecord latestLoanRecord;

    public double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public LoanRecord getLatestLoanRecord() {
        return latestLoanRecord;
    }

    public void setLatestLoanRecord(LoanRecord latestLoanRecord) {
        this.latestLoanRecord = latestLoanRecord;
    }

    @Override
    public String toString() {
        return "LoanSummary [totalBalance=" + totalBalance + ", totalPayment=" + totalPayment + ", totalPaid="
            + totalPaid + ", latestLoanRecord=" + latestLoanRecord + "]";
    }

}
