package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

import com.jbb.server.common.util.DateUtil;

public class LoginLog {

    private int accountId;
    private String username;
    private String nickname;
    private Timestamp loginDate;
    private String ipAddress;
    private String province;
    private String city;
    private String detail;

    public LoginLog() {

    }

    public LoginLog(int accountId, String ipAddress) {
        this.accountId = accountId;
        this.ipAddress = ipAddress;
        this.loginDate = DateUtil.getCurrentTimeStamp();
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Timestamp getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Timestamp loginDate) {
        this.loginDate = loginDate;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "LoginLog [accountId=" + accountId + ", username=" + username + ", nickname=" + nickname + ", loginDate="
            + loginDate + ", ipAddress=" + ipAddress + ", province=" + province + ", city=" + city + ", detail="
            + detail + "]";
    }

}
