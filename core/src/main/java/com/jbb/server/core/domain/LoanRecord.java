package com.jbb.server.core.domain;

import java.sql.Timestamp;
import java.util.List;

public class LoanRecord {
    /**
     * 借款记录ID
     */
    private int loanRecordId;
    /**
     * 用户ID
     */
    private int userId;
    /**
     * 贷款平台Id
     */
    private int platformId;
    /**
     * 借款类型
     */
    private int loanTypeId;
    /**
     * 平台订单ID
     */
    private String orderId;
    /**
     * 账单名称
     */
    private String name;
    /**
     * 借款金额
     */
    private double borrowingAmount;
    /**
     * 借款时间
     */
    private Timestamp borrowingDate;
    /**
     * 描述
     */
    private String description;
    /**
     * 是否删除
     */
    private boolean deleted;

    /**
     * 属性
     */
    private List<RecordAttr> attrs;

    /**
     * 还款记录
     */
    List<PaymentRecord> paymentRecords;

    public LoanRecord() {}

    public int getLoanRecordId() {
        return loanRecordId;
    }

    public void setLoanRecordId(int loanRecordId) {
        this.loanRecordId = loanRecordId;
    }

    public int getUserId() {
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

    public int getLoanTypeId() {
        return loanTypeId;
    }

    public void setLoanTypeId(int loanTypeId) {
        this.loanTypeId = loanTypeId;
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

    public double getBorrowingAmount() {
        return borrowingAmount;
    }

    public void setBorrowingAmount(double borrowingAmount) {
        this.borrowingAmount = borrowingAmount;
    }

    public Timestamp getBorrowingDate() {
        return borrowingDate;
    }

    public void setBorrowingDate(Timestamp borrowingDate) {
        this.borrowingDate = borrowingDate;
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

    public List<RecordAttr> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<RecordAttr> attrs) {
        this.attrs = attrs;
    }

    public List<PaymentRecord> getPaymentRecords() {
        return paymentRecords;
    }

    public void setPaymentRecords(List<PaymentRecord> paymentRecords) {
        this.paymentRecords = paymentRecords;
    }

    @Override
    public String toString() {
        return "LoanRecord [loanRecordId=" + loanRecordId + ", userId=" + userId + ", platformId=" + platformId
            + ", loanTypeId=" + loanTypeId + ", orderId=" + orderId + ", name=" + name + ", borrowingAmount="
            + borrowingAmount + ", borrowingDate=" + borrowingDate + ", description=" + description + ", deleted="
            + deleted + ", attrs=" + attrs + ", paymentRecords=" + paymentRecords + "]";
    }

}
