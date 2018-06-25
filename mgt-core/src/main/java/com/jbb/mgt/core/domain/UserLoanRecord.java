package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoanRecord {

    private int loanId;
    private String iouCode;
    private Integer applyId;
    private Integer accountId;
    private Integer userId;
    private Integer status;
    private Integer iouStatus;
    private Integer iouPlatformId;
    private String platformName;
    private Integer borrowingAmount;
    private Integer annualRate;
    private Timestamp borrowingDate;
    private Integer borrowingDays;
    private Timestamp repaymentDate;
    private Integer loanAccId;
    private Integer loanAmount;
    private Timestamp loanDate;
    private Integer collectorAccId;
    private Integer repayAmount;
    private Timestamp creationDate;
    private Timestamp updateDate;
    private Timestamp actualRepaymentDate;
    private Timestamp extentionDate;

    // 以下对象扩展
    private User user; // 借款人
    private Account account; // 债权人
    private UserApplyRecord userApplyRecord; // 申请记录
    private Account loanAccount; // 放款人
    private Account collector; // 催收人
    private Account assignAccount;
    private Account initAccount;
    private Account finalAccount;
    private Channel channel;

    public Timestamp getExtentionDate() {
        return extentionDate;
    }

    public void setExtentionDate(Timestamp extentionDate) {
        this.extentionDate = extentionDate;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getIouCode() {
        return iouCode;
    }

    public void setIouCode(String iouCode) {
        this.iouCode = iouCode;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIouStatus() {
        return iouStatus;
    }

    public void setIouStatus(Integer iouStatus) {
        this.iouStatus = iouStatus;
    }

    public Integer getIouPlatformId() {
        return iouPlatformId;
    }

    public void setIouPlatformId(Integer iouPlatformId) {
        this.iouPlatformId = iouPlatformId;
    }

    public Integer getBorrowingAmount() {
        return borrowingAmount;
    }

    public void setBorrowingAmount(Integer borrowingAmount) {
        this.borrowingAmount = borrowingAmount;
    }

    public Integer getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(Integer annualRate) {
        this.annualRate = annualRate;
    }

    public Timestamp getBorrowingDate() {
        return borrowingDate;
    }

    public void setBorrowingDate(Timestamp borrowingDate) {
        this.borrowingDate = borrowingDate;
    }

    public Integer getBorrowingDays() {
        return borrowingDays;
    }

    public void setBorrowingDays(Integer borrowingDays) {
        this.borrowingDays = borrowingDays;
    }

    public Timestamp getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(Timestamp repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public Integer getLoanAccId() {
        return loanAccId;
    }

    public void setLoanAccId(Integer loanAccId) {
        this.loanAccId = loanAccId;
    }

    public Integer getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Integer loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Timestamp getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Timestamp loanDate) {
        this.loanDate = loanDate;
    }

    public Integer getCollectorAccId() {
        return collectorAccId;
    }

    public void setCollectorAccId(Integer collectorAccId) {
        this.collectorAccId = collectorAccId;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public Timestamp getActualRepaymentDate() {
        return actualRepaymentDate;
    }

    public void setActualRepaymentDate(Timestamp actualRepaymentDate) {
        this.actualRepaymentDate = actualRepaymentDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public UserApplyRecord getUserApplyRecord() {
        return userApplyRecord;
    }

    public void setUserApplyRecord(UserApplyRecord userApplyRecord) {
        this.userApplyRecord = userApplyRecord;
    }

    public Account getLoanAccount() {
        return loanAccount;
    }

    public void setLoanAccount(Account loanAccount) {
        this.loanAccount = loanAccount;
    }

    public Account getCollector() {
        return collector;
    }

    public void setCollector(Account collector) {
        this.collector = collector;
    }

    public Account getAssignAccount() {
        return assignAccount;
    }

    public void setAssignAccount(Account assignAccount) {
        this.assignAccount = assignAccount;
    }

    public Account getInitAccount() {
        return initAccount;
    }

    public void setInitAccount(Account initAccount) {
        this.initAccount = initAccount;
    }

    public Account getFinalAccount() {
        return finalAccount;
    }

    public void setFinalAccount(Account finalAccount) {
        this.finalAccount = finalAccount;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public Integer getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(Integer repayAmount) {
        this.repayAmount = repayAmount;
    }

    @Override
    public String toString() {
        return "UserLoanRecord [loanId=" + loanId + ", iouCode=" + iouCode + ", applyId=" + applyId + ", accountId="
            + accountId + ", userId=" + userId + ", status=" + status + ", iouStatus=" + iouStatus + ", iouPlatformId="
            + iouPlatformId + ", platformName=" + platformName + ", borrowingAmount=" + borrowingAmount
            + ", annualRate=" + annualRate + ", borrowingDate=" + borrowingDate + ", borrowingDays=" + borrowingDays
            + ", repaymentDate=" + repaymentDate + ", loanAccId=" + loanAccId + ", loanAmount=" + loanAmount
            + ", loanDate=" + loanDate + ", collectorAccId=" + collectorAccId + ", repayAmount=" + repayAmount
            + ", creationDate=" + creationDate + ", updateDate=" + updateDate + ", actualRepaymentDate="
            + actualRepaymentDate + ", user=" + user + ", account=" + account + ", userApplyRecord=" + userApplyRecord
            + ", loanAccount=" + loanAccount + ", collector=" + collector + ", assignAccount=" + assignAccount
            + ", initAccount=" + initAccount + ", finalAccount=" + finalAccount + ", channel=" + channel + "]";
    }

}
