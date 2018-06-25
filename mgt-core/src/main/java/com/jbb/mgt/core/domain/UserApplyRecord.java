package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.common.util.DateUtil;

/**
 * UserApplyRecord实体类
 * 
 * @author wyq
 * @date 2018/04/24
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserApplyRecord {

    /** 申请ID */
    private int applyId;
    private int userId;
    private int orgId;
    private int status;
    private Integer assignAccId;
    private Timestamp assingDate;
    private Integer initAccId;
    private Integer initAmount;
    private Timestamp initDate;
    private Integer finalAccId;
    private Integer finalAmount;
    private Timestamp finalDate;
    private Integer loanAccId;
    private Integer loanAmount;
    private Timestamp loanDate;
    private String initMark;
    private String finalMark;
    private Timestamp creationDate;
    private Timestamp hangupDate;
    private Timestamp rejectDate;

    /** 用户 */
    private User user;
    private Account assignAccount;
    private Account initAccount;
    private Account finalAccount;
    private Account loanAccount;
    private Channel channel;

    public UserApplyRecord() {
        super();
    }

    public UserApplyRecord(int userId, int orgId) {
        super();
        this.userId = userId;
        this.orgId = orgId;
        this.creationDate = DateUtil.getCurrentTimeStamp();
    }

    public int getApplyId() {
        return applyId;
    }

    public void setApplyId(int applyId) {
        this.applyId = applyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getAssignAccId() {
        return assignAccId;
    }

    public void setAssignAccId(Integer assignAccId) {
        this.assignAccId = assignAccId;
    }

    public Timestamp getAssingDate() {
        return assingDate;
    }

    public void setAssingDate(Timestamp assingDate) {
        this.assingDate = assingDate;
    }

    public Integer getInitAccId() {
        return initAccId;
    }

    public void setInitAccId(Integer initAccId) {
        this.initAccId = initAccId;
    }

    public Integer getInitAmount() {
        return initAmount;
    }

    public void setInitAmount(Integer initAmount) {
        this.initAmount = initAmount;
    }

    public Timestamp getInitDate() {
        return initDate;
    }

    public void setInitDate(Timestamp initDate) {
        this.initDate = initDate;
    }

    public Integer getFinalAccId() {
        return finalAccId;
    }

    public void setFinalAccId(Integer finalAccId) {
        this.finalAccId = finalAccId;
    }

    public Integer getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(Integer finalAmount) {
        this.finalAmount = finalAmount;
    }

    public Timestamp getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Timestamp finalDate) {
        this.finalDate = finalDate;
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

    public String getInitMark() {
        return initMark;
    }

    public void setInitMark(String initMark) {
        this.initMark = initMark;
    }

    public String getFinalMark() {
        return finalMark;
    }

    public void setFinalMark(String finalMark) {
        this.finalMark = finalMark;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getHangupDate() {
        return hangupDate;
    }

    public void setHangupDate(Timestamp hangupDate) {
        this.hangupDate = hangupDate;
    }

    public Timestamp getRejectDate() {
        return rejectDate;
    }

    public void setRejectDate(Timestamp rejectDate) {
        this.rejectDate = rejectDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Account getLoanAccount() {
        return loanAccount;
    }

    public void setLoanAccount(Account loanAccount) {
        this.loanAccount = loanAccount;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "UserApplyRecord [applyId=" + applyId + ", userId=" + userId + ", orgId=" + orgId + ", status=" + status
            + ", assignAccId=" + assignAccId + ", assingDate=" + assingDate + ", initAccId=" + initAccId
            + ", initAmount=" + initAmount + ", initDate=" + initDate + ", finalAccId=" + finalAccId + ", finalAmount="
            + finalAmount + ", finalDate=" + finalDate + ", loanAccId=" + loanAccId + ", loanAmount=" + loanAmount
            + ", loanDate=" + loanDate + ", initMark=" + initMark + ", finalMark=" + finalMark + ", creationDate="
            + creationDate + ", hangupDate=" + hangupDate + ", rejectDate=" + rejectDate + ", user=" + user
            + ", assignAccount=" + assignAccount + ", initAccount=" + initAccount + ", finalAccount=" + finalAccount
            + ", loanAccount=" + loanAccount + ", channel=" + channel + "]";
    }

}
