package com.jbb.server.core.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.shared.rs.Util;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Iou {
    private String iouCode;
    private int borrowerId;
    private Integer lenderId;
    private double borrowingAmount;

    @JsonIgnore
    private Timestamp borrowingDate;
    @JsonProperty("borrowingDate")
    private String borrowingDateStr;
    private Long borrowingDateTs;

    @JsonIgnore
    private Timestamp repaymentDate;
    @JsonProperty("repaymentDate")
    private String repaymentDateStr;
    private Long repaymentDateTs;

    @JsonIgnore
    private Timestamp extensionDate;
    @JsonProperty("extensionDate")
    private String extensionDateStr;
    private Long extensionDateTs;

    private double annualRate;
    private String purpose;

    @JsonIgnore
    private Timestamp creationDate;
    @JsonProperty("creationDate")
    private String creationDateStr;
    private Long creationDateTs;

    @JsonIgnore
    private Timestamp effectiveDate;
    @JsonProperty("effectiveDate")
    private String effectiveDateStr;
    private Long effectiveDateTs;

    // 借条状态：0发布，1生效，2修改申请，3出借人确认修改，4出借人拒绝修改，5申请延期，6出借人确认延期，7出借人拒绝延期，8申请已还，9出借人确认已还，10出借人拒绝已还
    private int status;

    @JsonIgnore
    private Timestamp lastUpdateStatusDate;
    @JsonProperty("lastUpdateStatusDate")
    private String lastUpdateStatusDateStr;
    private Long lastUpdateStatusDateTs;

    private boolean lenderDeleted;
    private String device;
    private boolean deleted;

    // 计算字段
    // 利息
    private double interest;
    // 借款天数
    private int days;
    // 离到期日还有多少天
    private int daysOfRemain;
    // 延期天数
    private int daysOfDelay;

    // 借款人基本信息
    private UserBasic borrower;

    // 借款人基本信息
    private UserBasic lender;

    // 用户的意向情况, 借条中心， 出借人列表->待确认
    private Integer intendStatus;

    // 借条中心， 借款人列表->待确认， 意向出借人数量
    private Integer intendUserCnt;

    public Iou() {}

    public Iou(String iouCode, int borrowerId, int lenderId, double borrowingAmount, double annualRate,
        String purpose) {
        this.iouCode = iouCode;
        this.borrowerId = borrowerId;
        this.lenderId = lenderId;
        this.borrowingAmount = borrowingAmount;
        this.annualRate = annualRate;
        this.purpose = purpose;
    }

    public void prepareCalFields() {
        this.days = DateUtil.calDays(this.borrowingDateTs, this.repaymentDateTs);
        this.interest = this.borrowingAmount * this.days * this.annualRate / 365 / 100;
        this.daysOfRemain = DateUtil.calDays(DateUtil.getCurrentTime(), this.repaymentDateTs);

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

    public Integer getLenderId() {
        return lenderId;
    }

    public void setLenderId(Integer lenderId) {
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

    public void setBorrowingDate(Timestamp borrowingDate) {
        this.borrowingDate = borrowingDate;
        this.borrowingDateStr = Util.printDateTime(borrowingDate);
        this.borrowingDateTs = Util.getTimeMs(borrowingDate);
    }

    public Timestamp getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(Timestamp repaymentDate) {
        this.repaymentDate = repaymentDate;
        this.repaymentDateStr = Util.printDateTime(repaymentDate);
        this.repaymentDateTs = Util.getTimeMs(repaymentDate);
    }

    public Timestamp getExtensionDate() {
        return extensionDate;
    }

    public void setExtensionDate(Timestamp extensionDate) {
        this.extensionDate = extensionDate;
        this.extensionDateStr = Util.printDateTime(extensionDate);
        this.extensionDateTs = Util.getTimeMs(extensionDate);
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

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
        this.creationDateStr = Util.printDateTime(creationDate);
        this.creationDateTs = Util.getTimeMs(creationDate);
    }

    public Timestamp getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Timestamp effectiveDate) {
        this.effectiveDate = effectiveDate;
        this.effectiveDateStr = Util.printDateTime(effectiveDate);
        this.effectiveDateTs = Util.getTimeMs(effectiveDate);
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

    public void setLastUpdateStatusDate(Timestamp lastUpdateStatusDate) {
        this.lastUpdateStatusDate = lastUpdateStatusDate;
        this.lastUpdateStatusDateStr = Util.printDateTime(lastUpdateStatusDate);
        this.lastUpdateStatusDateTs = Util.getTimeMs(lastUpdateStatusDate);
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

    public String getBorrowingDateStr() {
        return borrowingDateStr;
    }

    public void setBorrowingDateStr(String borrowingDateStr) {
        this.borrowingDateStr = borrowingDateStr;
    }

    public Long getBorrowingDateTs() {
        return borrowingDateTs;
    }

    public void setBorrowingDateTs(Long borrowingDateTs) {
        this.borrowingDateTs = borrowingDateTs;
    }

    public String getRepaymentDateStr() {
        return repaymentDateStr;
    }

    public void setRepaymentDateStr(String repaymentDateStr) {
        this.repaymentDateStr = repaymentDateStr;
    }

    public Long getRepaymentDateTs() {
        return repaymentDateTs;
    }

    public void setRepaymentDateTs(Long repaymentDateTs) {
        this.repaymentDateTs = repaymentDateTs;
    }

    public String getExtensionDateStr() {
        return extensionDateStr;
    }

    public void setExtensionDateStr(String extensionDateStr) {
        this.extensionDateStr = extensionDateStr;
    }

    public Long getExtensionDateTs() {
        return extensionDateTs;
    }

    public void setExtensionDateTs(Long extensionDateTs) {
        this.extensionDateTs = extensionDateTs;
    }

    public String getCreationDateStr() {
        return creationDateStr;
    }

    public void setCreationDateStr(String creationDateStr) {
        this.creationDateStr = creationDateStr;
    }

    public Long getCreationDateTs() {
        return creationDateTs;
    }

    public void setCreationDateTs(Long creationDateTs) {
        this.creationDateTs = creationDateTs;
    }

    public String getEffectiveDateStr() {
        return effectiveDateStr;
    }

    public void setEffectiveDateStr(String effectiveDateStr) {
        this.effectiveDateStr = effectiveDateStr;
    }

    public Long getEffectiveDateTs() {
        return effectiveDateTs;
    }

    public void setEffectiveDateTs(Long effectiveDateTs) {
        this.effectiveDateTs = effectiveDateTs;
    }

    public String getLastUpdateStatusDateStr() {
        return lastUpdateStatusDateStr;
    }

    public void setLastUpdateStatusDateStr(String lastUpdateStatusDateStr) {
        this.lastUpdateStatusDateStr = lastUpdateStatusDateStr;
    }

    public Long getLastUpdateStatusDateTs() {
        return lastUpdateStatusDateTs;
    }

    public void setLastUpdateStatusDateTs(Long lastUpdateStatusDateTs) {
        this.lastUpdateStatusDateTs = lastUpdateStatusDateTs;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getDaysOfRemain() {
        return daysOfRemain;
    }

    public void setDaysOfRemain(int daysOfRemain) {
        this.daysOfRemain = daysOfRemain;
    }

    public int getDaysOfDelay() {
        return daysOfDelay;
    }

    public void setDaysOfDelay(int daysOfDelay) {
        this.daysOfDelay = daysOfDelay;
    }

    public UserBasic getBorrower() {
        return borrower;
    }

    public void setBorrower(UserBasic borrower) {
        this.borrower = borrower;
    }

    public Integer getIntendStatus() {
        return intendStatus;
    }

    public void setIntendStatus(Integer intendStatus) {
        this.intendStatus = intendStatus;
    }

    public Integer getIntendUserCnt() {
        return intendUserCnt;
    }

    public void setIntendUserCnt(Integer intendUserCnt) {
        this.intendUserCnt = intendUserCnt;
    }

    public UserBasic getLender() {
        return lender;
    }

    public void setLender(UserBasic lender) {
        this.lender = lender;
    }

    @Override
    public String toString() {
        return "Iou [iouCode=" + iouCode + ", borrowerId=" + borrowerId + ", lenderId=" + lenderId
            + ", borrowingAmount=" + borrowingAmount + ", borrowingDate=" + borrowingDate + ", borrowingDateStr="
            + borrowingDateStr + ", borrowingDateTs=" + borrowingDateTs + ", repaymentDate=" + repaymentDate
            + ", repaymentDateStr=" + repaymentDateStr + ", repaymentDateTs=" + repaymentDateTs + ", extensionDate="
            + extensionDate + ", extensionDateStr=" + extensionDateStr + ", extensionDateTs=" + extensionDateTs
            + ", annualRate=" + annualRate + ", purpose=" + purpose + ", creationDate=" + creationDate
            + ", creationDateStr=" + creationDateStr + ", creationDateTs=" + creationDateTs + ", effectiveDate="
            + effectiveDate + ", effectiveDateStr=" + effectiveDateStr + ", effectiveDateTs=" + effectiveDateTs
            + ", status=" + status + ", lastUpdateStatusDate=" + lastUpdateStatusDate + ", lastUpdateStatusDateStr="
            + lastUpdateStatusDateStr + ", lastUpdateStatusDateTs=" + lastUpdateStatusDateTs + ", lenderDeleted="
            + lenderDeleted + ", device=" + device + ", deleted=" + deleted + ", interest=" + interest + ", days="
            + days + ", daysOfRemain=" + daysOfRemain + ", daysOfDelay=" + daysOfDelay + ", borrower=" + borrower
            + ", lender=" + lender + ", intendStatus=" + intendStatus + ", intendUserCnt=" + intendUserCnt + "]";
    }

}
