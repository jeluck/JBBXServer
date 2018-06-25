package com.jbb.server.core.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ious {
    private String iouCode;
    private int borrowerId;
    private int lenderId;
    private Long borrowingDateLong;
    private Long repaymentDateLong;
    private Long extensionDateLong;
    
    private double borrowingAmount;
    @JsonIgnore
    private Timestamp borrowingDate;
    @JsonIgnore
    private Timestamp repaymentDate;
    @JsonIgnore
    private Timestamp extensionDate;

    private double annualRate;
    private String purpose;
    private Long creationDate;
    private Long effectiveDate;
    //借条状态：0发布，1生效，2修改申请，3出借人确认修改，4出借人拒绝修改，5申请延期，6出借人确认延期，7出借人拒绝延期，8申请已还，9出借人确认已还，10出借人拒绝已还
    private int status;
    private Timestamp lastUpdateStatusDate;
    @JsonProperty("extensionDate")
    private Long lastUpdateStatusDateLong;
    private boolean lenderDeleted;
    private String device;
    private boolean deleted;

    public Ious() {}

    public Ious(String iouCode, int borrowerId, int lenderId, double borrowingAmount, double annualRate,
        String purpose) {
        this.iouCode = iouCode;
        this.borrowerId = borrowerId;
        this.lenderId = lenderId;
        this.borrowingAmount = borrowingAmount;
        this.annualRate = annualRate;
        this.purpose = purpose;
    }

    public String getIouCode() {
        return iouCode;
    }

    public void setIouCode(String iouCode) {
        this.iouCode = iouCode;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public int getLenderId() {
        return lenderId;
    }

    public void setLenderId(int lenderId) {
        this.lenderId = lenderId;
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

    public void setBorrowingDate() {
        this.borrowingDate =new Timestamp(this.borrowingDateLong);
    }

    public Timestamp getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate() {
        this.repaymentDate = new Timestamp(this.repaymentDateLong);
    }

    public Timestamp getExtensionDate() {
        return extensionDate;
    }

    public void setExtensionDate() {
        this.extensionDate = new Timestamp(this.extensionDateLong);
    }

    public Long getBorrowingDateLong() {
        return borrowingDateLong;
    }

    public void setBorrowingDateLong(Long borrowingDateLong) {
        this.borrowingDateLong = borrowingDateLong;
    }

    public Long getRepaymentDateLong() {
        return repaymentDateLong;
    }

    public void setRepaymentDateLong(Long repaymentDateLong) {
        this.repaymentDateLong = repaymentDateLong;
    }

    public Long getExtensionDateLong() {
        return extensionDateLong;
    }

    public void setExtensionDateLong(Long extensionDateLong) {
        this.extensionDateLong = extensionDateLong;
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

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public Long getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Long effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getLastUpdateStatusDate() {
        return lastUpdateStatusDate;
    }

    public void setLastUpdateStatusDate() {
        this.lastUpdateStatusDate = new Timestamp(this.lastUpdateStatusDateLong);
    }

    public Long getLastUpdateStatusDateLong() {
        return lastUpdateStatusDateLong;
    }

    public void setLastUpdateStatusDateLong(Long lastUpdateStatusDateLong) {
        this.lastUpdateStatusDateLong = lastUpdateStatusDateLong;
    }

    public boolean isLenderDeleted() {
        return lenderDeleted;
    }

    public void setLenderDeleted(boolean lenderDeleted) {
        this.lenderDeleted = lenderDeleted;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Ious [iouCode=" + iouCode + ", borrowerId=" + borrowerId + ", lenderId=" + lenderId
            + ", borrowingAmount=" + borrowingAmount + ", borrowingDate=" + borrowingDate + ", repaymentDate="
            + repaymentDate + ", extensionDate=" + extensionDate + ", annualRate=" + annualRate + ", purpose=" + purpose
            + ", creationDate=" + creationDate + ", effectiveDate=" + effectiveDate + ", status=" + status
            + ", lastUpdateStatusDate=" + lastUpdateStatusDate + ", lenderDeleted=" + lenderDeleted + ", device="
            + device + ", deleted=" + deleted + "]";
    }
}
