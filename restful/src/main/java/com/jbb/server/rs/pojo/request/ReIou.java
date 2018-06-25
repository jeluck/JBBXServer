package com.jbb.server.rs.pojo.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReIou {
    @JsonProperty(required = true)
    private double borrowingAmount;
    @JsonProperty(required = false)
    private Long repaymentDateTs;
    @JsonProperty(required = false)
    private Integer days;
    @JsonProperty(required = true)
    private double annualRate;
    @JsonProperty(required = true)
    private String purpose;
    @JsonProperty(required = true)
    private String device;
    @JsonProperty(required = true)
    private boolean flag;

    public double getBorrowingAmount() {
        return borrowingAmount;
    }

    public void setBorrowingAmount(double borrowingAmount) {
        this.borrowingAmount = borrowingAmount;
    }

    public Long getRepaymentDateTs() {
        return repaymentDateTs;
    }

    public void setRepaymentDateTs(Long repaymentDateTs) {
        this.repaymentDateTs = repaymentDateTs;
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

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    

    @Override
    public String toString() {
        return "ReIou [borrowingAmount=" + borrowingAmount + ", repaymentDateTs=" + repaymentDateTs + ", days=" + days
            + ", annualRate=" + annualRate + ", purpose=" + purpose + ", device=" + device + ", flag=" + flag + "]";
    }

}
