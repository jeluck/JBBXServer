package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.common.util.DateUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoanRecordDetail {
    private int loanId;// 贷款记录编号
    private Integer opType;// 类型：// 操作类型， op_logs表对应
    private Integer amountType;// 类型：// 收入支出分类
    private Integer amount;// 金额(分)
    private Integer accountId;// 操作人
    private Timestamp opDate;// 流水时间

    public UserLoanRecordDetail() {
        super();
    }

    public UserLoanRecordDetail(int loanId, Integer opType, Integer amountType, Integer amount, Integer accountId) {
        super();
        this.loanId = loanId;
        this.opType = opType;
        this.amount = amount;
        this.amountType = amountType;
        this.accountId = accountId;
        this.opDate = DateUtil.getCurrentTimeStamp();
    }

    public int getLoanId() {
        return loanId;
    }

    public Integer getOpType() {
        return opType;
    }

    public Integer getAmountType() {
        return amountType;
    }

    public void setAmountType(Integer amountType) {
        this.amountType = amountType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Timestamp getOpDate() {
        return opDate;
    }

    public void setOpDate(Timestamp opDate) {
        this.opDate = opDate;
    }

    @Override
    public String toString() {
        return "UserLoanRecordDetail [loanId=" + loanId + ", opType=" + opType + ", amountType=" + amountType
            + ", amount=" + amount + ", accountId=" + accountId + ", opDate=" + opDate + "]";
    }

}
