package com.jbb.mgt.core.domain;

import java.sql.Timestamp;
import java.util.Date;

public class UserLoginLog {
	
	private int userId;
	private Timestamp loginDate;
	private String ipAddress;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getLoginDate() {
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
	
}
