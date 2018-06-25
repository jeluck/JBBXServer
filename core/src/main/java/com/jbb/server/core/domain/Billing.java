package com.jbb.server.core.domain;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbb.server.shared.rs.Util;

public class Billing {

    private Integer billingId;
    private int userId;
    private int platformId;
    private String platformName;
    private String orderId;
    private String name;
    private double bAmount;
    @JsonIgnore
    private Timestamp bDate;
    @JsonProperty("bDate")
    private String bDateStr;
    private Long bDateMs;
    private int loanTypeId;
    private int repeats;
    private int startNo;
    private double pAmount;
    @JsonIgnore
    private Timestamp pDate;
    @JsonProperty("pDate")
    private String pDateStr;
    private Long pDateMs;
    private double rate;
    private String description;
    private boolean deleted;
    private List<BillingDetail> details;

    public Integer getBillingId() {
        return billingId;
    }

    public void setBillingId(Integer billingId) {
        this.billingId = billingId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getbAmount() {
        return bAmount;
    }

    public void setbAmount(double bAmount) {
        this.bAmount = bAmount;
    }

    public Timestamp getbDate() {
        return bDate;
    }

    public void setbDate(Timestamp bDate) {
        this.bDate = bDate;
        this.bDateStr = Util.printDateTime(bDate);
        this.bDateMs = Util.getTimeMs(bDate);
    }

    public int getLoanTypeId() {
        return loanTypeId;
    }

    public void setLoanTypeId(int loanTypeId) {
        this.loanTypeId = loanTypeId;
    }

    public int getRepeats() {
        return repeats;
    }

    public void setRepeats(int repeats) {
        this.repeats = repeats;
    }

    public int getStartNo() {
        return startNo;
    }

    public void setStartNo(int startNo) {
        this.startNo = startNo;
    }

    public double getpAmount() {
        return pAmount;
    }

    public void setpAmount(double pAmount) {
        this.pAmount = pAmount;
    }

    public Timestamp getpDate() {
        return pDate;
    }

    public void setpDate(Timestamp pDate) {
        this.pDate = pDate;
        this.pDateStr = Util.printDateTime(pDate);
        this.pDateMs = Util.getTimeMs(pDate);
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<BillingDetail> getDetails() {
        return details;
    }

    public void setDetails(List<BillingDetail> details) {
        this.details = details;
    }

    public String getbDateStr() {
        return bDateStr;
    }

    public Long getbDateMs() {
        return bDateMs;
    }

    public String getpDateStr() {
        return pDateStr;
    }

    public Long getpDateMs() {
        return pDateMs;
    }
    
    

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @Override
    public String toString() {
        return "Billing [billingId=" + billingId + ", userId=" + userId + ", platformId=" + platformId + ", platformName=" + platformName + ", orderId="
            + orderId + ", name=" + name + ", bAmount=" + bAmount + ", bDate=" + bDate + ", loanTypeId=" + loanTypeId
            + ", repeats=" + repeats + ", startNo=" + startNo + ", pAmount=" + pAmount + ", pDate=" + pDate + ", rate="
            + rate + ", description=" + description + ", deleted=" + deleted + ", details=" + details + "]";
    }

}
