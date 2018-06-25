package com.jbb.server.rs.pojo.request;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbb.server.core.domain.Billing;

public class ReBilling {

    private Integer billingId;
    @JsonProperty(required = true)
    private int platformId;
    @JsonProperty(required = true)
    private String name;
    @JsonProperty(required = true)
    private Double bAmount;
    @JsonProperty(required = true)
    private Long bDate;
    @JsonProperty(required = true)
    private int loanTypeId;
    private Double pAmount;
    private Long pDate;
    private String description;

    public Billing generateBilling() {

        Billing billing = new Billing();
        billing.setBillingId(this.getBillingId());
        billing.setbAmount(this.getbAmount());
        billing.setbDate(this.bDate != null ? new Timestamp(this.bDate) : null);
        billing.setName(this.getName());
        billing.setDescription(this.getDescription());
        billing.setLoanTypeId(this.getLoanTypeId());
        billing.setpAmount(this.getpAmount());
        billing.setpDate(this.pDate != null ? new Timestamp(this.pDate) : null);
        billing.setPlatformId(this.getPlatformId());
        return billing;

    }

    public Integer getBillingId() {
        return billingId;
    }

    public void setBillingId(Integer billingId) {
        this.billingId = billingId;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
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

    public Long getbDate() {
        return bDate;
    }

    public void setbDate(Long bDate) {
        this.bDate = bDate;
    }

    public int getLoanTypeId() {
        return loanTypeId;
    }

    public void setLoanTypeId(int loanTypeId) {
        this.loanTypeId = loanTypeId;
    }

    public double getpAmount() {
        return pAmount;
    }

    public void setpAmount(double pAmount) {
        this.pAmount = pAmount;
    }

    public Long getpDate() {
        return pDate;
    }

    public void setpDate(Long pDate) {
        this.pDate = pDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ReBilling [platformId=" + platformId + ", billingId=" + billingId + ", name=" + name + ", bAmount="
            + bAmount + ", bDate=" + bDate + ", loanTypeId=" + loanTypeId + ", pAmount=" + pAmount + ", pDate=" + pDate
            + ", description=" + description + "]";
    }

}
