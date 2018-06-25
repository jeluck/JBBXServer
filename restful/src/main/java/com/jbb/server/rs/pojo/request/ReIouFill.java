package com.jbb.server.rs.pojo.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReIouFill {
    @JsonProperty(required = true)
    private double borrowingAmount;
    @JsonProperty(required = true)
    private String borrowingDate;
    @JsonProperty(required = true)
    private String repaymentDate;
    @JsonProperty(required = true)
    private double annualRate;
    @JsonProperty(required = true)
    private String purpose;
    @JsonProperty(required = false)
    private String device;
    @JsonProperty(required = true)
    private boolean flag;

    @JsonProperty(required = true)
    private int userId;

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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ReIouFill [borrowingAmount=" + borrowingAmount + ", borrowingDate=" + borrowingDate + ", repaymentDate="
            + repaymentDate + ", annualRate=" + annualRate + ", purpose=" + purpose + ", device=" + device + ", flag="
            + flag + ", userId=" + userId + "]";
    }

    
}
