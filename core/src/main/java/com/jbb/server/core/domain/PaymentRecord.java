package com.jbb.server.core.domain;

import java.sql.Timestamp;
import java.util.List;

public class PaymentRecord {
    /**
     *
     */
    private int paymentRecordId;
    /**
     * 借款记录
     */
    private int loanRecordId;
    /**
     * 用户ID
     */
    private int userId;
    /**
     * 还款金额
     */
    private double paymentAmount;
    /**
     * 还款日期
     */
    private Timestamp paymentDate;
    /**
     * 描述
     */
    private String description;
    /**
     * 是否删除
     */
    private boolean deleted;

    private List<RecordAttr> attrs;

    public PaymentRecord() {}

    public int getPaymentRecordId() {
        return paymentRecordId;
    }

    public void setPaymentRecordId(int paymentRecordId) {
        this.paymentRecordId = paymentRecordId;
    }

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

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isDeleted() {
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

    @Override
    public String toString() {
        return "PaymentRecord [paymentRecordId=" + paymentRecordId + ", loanRecordId=" + loanRecordId + ", userId="
            + userId + ", paymentAmount=" + paymentAmount + ", paymentDate=" + paymentDate + ", description="
            + description + ", deleted=" + deleted + ", attrs=" + attrs + "]";
    }

}
