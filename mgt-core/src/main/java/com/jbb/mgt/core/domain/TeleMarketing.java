package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

import com.jbb.server.common.util.DateUtil;

/**
 * 电销批次实体类
 *
 * @author wyq
 * @date 2018/4/29.
 */
public class TeleMarketing {
//    状态：0-导入；1-处理中；2-处理完成
    public static int STATUS_INIT = 0; 
    public static int STATUS_PROCESSING = 1;  
    public static int STATUS_DONE = 2; 
  
    
    private int batchId;
    private int accountId;
    private int cnt;
    private int validCnt;
    private int price;
    private int status;
    private Timestamp creationDate;

    public TeleMarketing(int batchId, int accountId, int cnt, int validCnt, int price, int status,
        Timestamp creationDate) {
        super();
        this.batchId = batchId;
        this.accountId = accountId;
        this.cnt = cnt;
        this.validCnt = validCnt;
        this.price = price;
        this.status = status;
        this.creationDate = creationDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getValidCnt() {
        return validCnt;
    }

    public void setValidCnt(int validCnt) {
        this.validCnt = validCnt;
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

    public TeleMarketing() {
        super();
    }

    @Override
    public String toString() {
        return "TeleMarketing [batchId=" + batchId + ", accountId=" + accountId + ", cnt=" + cnt + ", validCnt="
            + validCnt + ", price=" + price + ", status=" + status + ", creationDate=" + creationDate + "]";
    }
}
