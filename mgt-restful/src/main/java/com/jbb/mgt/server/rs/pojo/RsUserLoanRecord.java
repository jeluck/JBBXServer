 package com.jbb.mgt.server.rs.pojo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.Account;
import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.core.domain.UserApplyRecord;
import com.jbb.mgt.core.domain.UserLoanRecord;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsUserLoanRecord {
         private String loanId;// 贷款记录编号
         private Integer accountId;// 出借人
         private Integer userId;//借款人
         private Integer status;// 款项状态
         private Integer iouPlatformId;//借条平台
         private Integer borrowingAmount;// 借款金额
         private Integer annualRate;//年化利率
         private Date borrowingDate;// 借款时间
         private Integer borrowingDays;// 借款天数
         private Date repaymentDate;// 到期时间
         private User user;
         private Account account;
         private UserApplyRecord userApplyRecord;
         
         public UserApplyRecord getUserApplyRecord() {
            return userApplyRecord;
        }
        public void setUserApplyRecord(UserApplyRecord userApplyRecord) {
            this.userApplyRecord = userApplyRecord;
        }
        public RsUserLoanRecord() {}
         public RsUserLoanRecord(UserLoanRecord userLoanRecord) {
            super();
            this.loanId =String.valueOf(userLoanRecord.getLoanId());
            this.accountId = userLoanRecord.getAccountId();
            this.userId = userLoanRecord.getUserId();
            this.status = userLoanRecord.getStatus();
            this.iouPlatformId = userLoanRecord.getIouPlatformId();
            this.borrowingAmount = userLoanRecord.getBorrowingAmount();
            this.annualRate = userLoanRecord.getAnnualRate();
            this.borrowingDate = userLoanRecord.getBorrowingDate();
            this.borrowingDays = userLoanRecord.getBorrowingDays();
            this.repaymentDate = userLoanRecord.getRepaymentDate();
            this.user = userLoanRecord.getUser();
            this.account = userLoanRecord.getAccount();
            this.userApplyRecord = userLoanRecord.getUserApplyRecord();
        }
        public Account getAccount() {
             return account;
         }
         public void setAccount(Account account) {
             this.account = account;
         }
         public User getUser() {
             return user;
         }
         public void setUser(User user) {
             this.user = user;
         }
         public String getLoanId() {
             return loanId;
         }
         public void setLoanId(String loanId) {
             this.loanId = loanId;
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
         public Date getBorrowingDate() {
             return borrowingDate;
         }
         public void setBorrowingDate(Date borrowingDate) {
             this.borrowingDate = borrowingDate;
         }
         public Integer getBorrowingDays() {
             return borrowingDays;
         }
         public void setBorrowingDays(Integer borrowingDays) {
             this.borrowingDays = borrowingDays;
         }
         public Date getRepaymentDate() {
             return repaymentDate;
         }
         public void setRepaymentDate(Date repaymentDate) {
             this.repaymentDate = repaymentDate;
         }
         
         @Override
        public String toString() {
            return "RsUserLoanRecord [loanId=" + loanId + ", accountId=" + accountId + ", userId=" + userId
                + ", status=" + status + ", iouPlatformId=" + iouPlatformId + ", borrowingAmount=" + borrowingAmount
                + ", annualRate=" + annualRate + ", borrowingDate=" + borrowingDate + ", borrowingDays=" + borrowingDays
                + ", repaymentDate=" + repaymentDate + ", user=" + user + ", account=" + account + ", userApplyRecord="
                + userApplyRecord + "]";
        }
         
         
     }
