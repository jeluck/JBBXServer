package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

public class ChuangLanWoolCheck {
    private String mobile;
    private String tradeNo;
    private String chargesStatus;
    private String status;
    private String tag;
    private String ipAddrdss;
    private Timestamp updateDate;

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public String getMobile() {
        return mobile;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public String getChargesStatus() {
        return chargesStatus;
    }

    public String getStatus() {
        return status;
    }

    public String getTag() {
        return tag;
    }

    public String getIpAddrdss() {
        return ipAddrdss;
    }

    public void setIpAddrdss(String ipAddrdss) {
        this.ipAddrdss = ipAddrdss;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public void setChargesStatus(String chargesStatus) {
        this.chargesStatus = chargesStatus;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "ChuangLanWoolCheck [mobile=" + mobile + ", tradeNo=" + tradeNo + ", chargesStatus=" + chargesStatus
            + ", status=" + status + ", tag=" + tag + ", ipAddrdss=" + ipAddrdss + "]";
    }

    
    public String toCsvString() {
        return mobile + "," + tradeNo + "," + chargesStatus + "," + status + "," + tag + "," + ipAddrdss;
    }

}
