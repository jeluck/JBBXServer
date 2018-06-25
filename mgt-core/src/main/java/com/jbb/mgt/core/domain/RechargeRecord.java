 package com.jbb.mgt.core.domain;

import java.util.Date;

public class RechargeRecord {
     private String tradeNo;
     private Integer orgId;
     private Integer accountId;
     private String rechargeType;
     private String status;
     private Integer amount;
     private Date creationDate;
    public String getTradeNo() {
        return tradeNo;
    }
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }
    public Integer getOrgId() {
        return orgId;
    }
    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }
    public Integer getAccountId() {
        return accountId;
    }
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
    public String getRechargeType() {
        return rechargeType;
    }
    public void setRechargeType(String rechargeType) {
        this.rechargeType = rechargeType;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Integer getAmount() {
        return amount;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    @Override
    public String toString() {
        return "RechargeRecord [tradeNo=" + tradeNo + ", orgId=" + orgId + ", accountId=" + accountId
            + ", rechargeType=" + rechargeType + ", status=" + status + ", amount=" + amount + ", creationDate="
            + creationDate + "]";
    }
     
}
