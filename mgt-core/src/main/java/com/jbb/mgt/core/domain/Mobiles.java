package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

public class Mobiles {
    private Integer id;
    private String phoneNumber;// 手机号
    private String checkType;// 检测类型
    private String checkResult;// 检测结果
    private Timestamp checkDate;// 检测时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public Timestamp getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Timestamp checkDate) {
        this.checkDate = checkDate;
    }

    @Override
    public String toString() {
        return "Mobiles [id=" + id + ", phoneNumber=" + phoneNumber + ", checkType=" + checkType + ", checkResult="
            + checkResult + ", checkDate=" + checkDate + "]";
    }

}
