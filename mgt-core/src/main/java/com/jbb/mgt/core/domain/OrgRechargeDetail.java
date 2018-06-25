package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

public class OrgRechargeDetail {

    private String tradeNo;// 交易号
    private int accountId;// 消耗人员accountId
    private int opType;// 11 - 充值短信，12 - 充值流量， 21 - 消耗数据， 22-消耗短信
    private int amount;// 金额
    private int status;// 0 - 提交，1 - 记账成功
    private Timestamp creationDate;// 创建时间
    private String params; // 存储详情信息

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "OrgRechargeDetail [tradeNo=" + tradeNo + ", accountId=" + accountId + ", opType=" + opType + ", amount="
            + amount + ", status=" + status + ", creationDate=" + creationDate + "]";
    }

}
