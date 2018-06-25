package com.jbb.mgt.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;

/**
 * 统计量实体类
 *
 * @author wyq
 * @date 2018/5/25 09:31
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelStatistic {
    private Timestamp creationDate;
    /** 点击量 */
    private int clickCnt;
    /** 提交量 */
    private int submitCnt;
    /** 独立用户量 */
    private int uvCnt;
    /** 注册量 */
    private int registerCnt;
    /** 放款量 */
    private int loanCnt;
    /** 放款额 */
    private int loanAmount;
    /** 借款额 */
    private int borrowingAmount;

    public int getClickCnt() {
        return clickCnt;
    }

    public void setClickCnt(int clickCnt) {
        this.clickCnt = clickCnt;
    }

    public int getSubmitCnt() {
        return submitCnt;
    }

    public void setSubmitCnt(int submitCnt) {
        this.submitCnt = submitCnt;
    }

    public int getUvCnt() {
        return uvCnt;
    }

    public void setUvCnt(int uvCnt) {
        this.uvCnt = uvCnt;
    }

    public int getRegisterCnt() {
        return registerCnt;
    }

    public void setRegisterCnt(int registerCnt) {
        this.registerCnt = registerCnt;
    }

    public int getLoanCnt() {
        return loanCnt;
    }

    public void setLoanCnt(int loanCnt) {
        this.loanCnt = loanCnt;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(int loanAmount) {
        this.loanAmount = loanAmount;
    }

    public int getBorrowingAmount() {
        return borrowingAmount;
    }

    public void setBorrowingAmount(int borrowingAmount) {
        this.borrowingAmount = borrowingAmount;
    }

    public ChannelStatistic() {
        super();
    }

    public ChannelStatistic(int clickCnt, int submitCnt, int uvCnt, int registerCnt, int loanCnt) {
        this.clickCnt = clickCnt;
        this.submitCnt = submitCnt;
        this.uvCnt = uvCnt;
        this.registerCnt = registerCnt;
        this.loanCnt = loanCnt;
    }

}
