package com.jbb.server.rs.pojo.request;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbb.server.core.domain.Repayment;

public class ReRepayment {
    @JsonProperty(required = true)
    private int billingId;
    @JsonProperty(required = true)
    private int billingDetailId;
    @JsonProperty(required = true)
    private Long payDateMs;
    @JsonProperty(required = true)
    private Double amount;

    public Repayment prepareRepayment() {
        Repayment r = new Repayment();
        r.setAmount(this.amount);
        r.setBillingDetailId(this.getBillingDetailId());
        r.setRepayDate(this.payDateMs!=null?new Timestamp(payDateMs):null);
        return r;
    }

    public int getBillingId() {
        return billingId;
    }

    public void setBillingId(int billingId) {
        this.billingId = billingId;
    }

    public int getBillingDetailId() {
        return billingDetailId;
    }

    public void setBillingDetailId(int billingDetailId) {
        this.billingDetailId = billingDetailId;
    }

    public Long getPayDateMs() {
        return payDateMs;
    }

    public void setPayDateMs(Long payDateMs) {
        this.payDateMs = payDateMs;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ReRepayment [billingId=" + billingId + ", billingDetailId=" + billingDetailId + ", payDateMs=" + payDateMs
            + ", amount=" + amount + "]";
    }

}
