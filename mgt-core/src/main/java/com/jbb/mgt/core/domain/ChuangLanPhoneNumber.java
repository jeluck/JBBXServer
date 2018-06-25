package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

public class ChuangLanPhoneNumber {
    private String mobile;
    private Timestamp lastTime;
    private String area;
    private String numberType;
    private String chargesStatus;
    private String status;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Timestamp getLastTime() {
        return lastTime;
    }

    public void setLastTime(Timestamp lastTime) {
        this.lastTime = lastTime;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }

    public String getChargesStatus() {
        return chargesStatus;
    }

    public void setChargesStatus(String chargesStatus) {
        this.chargesStatus = chargesStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ChuangLanPhoneNumber [mobile=" + mobile + ", lastTime=" + lastTime + ", area=" + area + ", numberType="
            + numberType + ", chargesStatus=" + chargesStatus + ", status=" + status + "]";
    }

}
