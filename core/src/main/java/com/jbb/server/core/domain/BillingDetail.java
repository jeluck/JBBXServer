package com.jbb.server.core.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbb.server.shared.rs.Util;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillingDetail {

    private int billingDetailId;
    private int billingId;
    private int currentNo;
    private double balance;
    private double interest;
    @JsonIgnore
    private Timestamp returnDate;
    @JsonProperty("returnDate")
    private String returnDateStr;
    private Long returnDateMs;
    private double paymentSum;
    @JsonIgnore
    private Timestamp lastPaymentDate;
    @JsonProperty("lastPaymentDate")
    private String lastPaymentDateStr;
    private Long lastPaymentDateMs;
    private int status;

    public BillingDetail() {

    }

    public BillingDetail(int billingDetailId, int status) {
        this.billingDetailId = billingDetailId;
        this.status = status;
    }

    public BillingDetail(int billingDetailId, double paymentSum, Timestamp lastPaymentDate) {
        this.billingDetailId = billingDetailId;
        this.paymentSum = paymentSum;
        this.lastPaymentDate = lastPaymentDate;
    }

    public int getBillingDetailId() {
        return billingDetailId;
    }

    public void setBillingDetailId(int billingDetailId) {
        this.billingDetailId = billingDetailId;
    }

    public int getBillingId() {
        return billingId;
    }

    public void setBillingId(int billingId) {
        this.billingId = billingId;
    }

    public int getCurrentNo() {
        return currentNo;
    }

    public void setCurrentNo(int currentNo) {
        this.currentNo = currentNo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Timestamp getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Timestamp returnDate) {
        this.returnDate = returnDate;
        this.returnDateStr = Util.printDateTime(returnDate);
        this.returnDateMs = Util.getTimeMs(returnDate);
    }

    public double getPaymentSum() {
        return paymentSum;
    }

    public void setPaymentSum(double paymentSum) {
        this.paymentSum = paymentSum;
    }

    public Timestamp getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(Timestamp lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
        this.lastPaymentDateStr = Util.printDateTime(lastPaymentDate);
        this.lastPaymentDateMs = Util.getTimeMs(lastPaymentDate);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public String getReturnDateStr() {
        return this.returnDateStr;
    }

    public Long getReturnDateMs() {
        return this.returnDateMs;
    }

    public String getLastPaymentDateStr() {
        return this.lastPaymentDateStr;
    }

    public Long getLastPaymentDateMs() {
        return this.lastPaymentDateMs;
    }

    @Override
    public String toString() {
        return "BillingDetail [billingDetailId=" + billingDetailId + ", billingId=" + billingId + ", currentNo="
            + currentNo + ", balance=" + balance + ", returnDate=" + returnDate + ", paymentSum=" + paymentSum
            + ", lastPaymentDate=" + lastPaymentDate + ", status=" + status + ", interest=" + interest + "]";
    }

}
