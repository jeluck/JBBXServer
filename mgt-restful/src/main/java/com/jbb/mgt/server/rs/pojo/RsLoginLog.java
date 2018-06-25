package com.jbb.mgt.server.rs.pojo;

import com.jbb.mgt.core.domain.LoginLog;
import com.jbb.server.shared.rs.Util;

public class RsLoginLog {
    private int accountId;
    private String username;
    private String nickname;
    private Long loginDateMs;
    private String ipAddress;
    private String province;
    private String city;

    public RsLoginLog() {

    }

    public RsLoginLog(LoginLog log) {
        this.accountId = log.getAccountId();
        this.ipAddress = log.getIpAddress();
        this.loginDateMs = Util.getTimeMs(log.getLoginDate());
        this.username = log.getUsername();
        this.nickname = log.getNickname();
        this.province = log.getProvince();
        this.city = log.getCity();
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

    public Long getLoginDateMs() {
        return loginDateMs;
    }

    public void setLoginDateMs(Long loginDateMs) {
        this.loginDateMs = loginDateMs;
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

}
