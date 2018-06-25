package com.jbb.server.core.domain;

import java.sql.Timestamp;

public class UserLoginLog {
    private Integer userId;

    private String platform;

    private String remoteAddress;

    private Timestamp loginTime;
    
    
    public UserLoginLog() {
		super();
	}

	public UserLoginLog(Integer userId, String platform, String remoteAddress, Timestamp loginTime) {
		super();
		this.userId = userId;
		this.platform = platform;
		this.remoteAddress = remoteAddress;
		this.loginTime = loginTime;
	}

	public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform == null ? null : platform.trim();
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress == null ? null : remoteAddress.trim();
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }
}