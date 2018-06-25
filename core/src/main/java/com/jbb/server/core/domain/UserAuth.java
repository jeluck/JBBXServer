package com.jbb.server.core.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Vincent Tang
 */
public class UserAuth {
	private String sessionId;

	private String openId;

	private String secretKey;

	private String sessionKey;

	private Timestamp expiry;

	private String unionId;

	public UserAuth() {
		super();
	}

	public UserAuth(String openId, String unionId, String sessionId, String sessionKey, String secretKey,
			Timestamp expiry) {
		super();
		this.sessionId = sessionId;
		this.openId = openId;
		this.unionId = unionId;
		this.secretKey = secretKey;
		this.sessionKey = sessionKey;
		this.expiry = expiry;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId == null ? null : sessionId.trim();
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId == null ? null : openId.trim();
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey == null ? null : secretKey.trim();
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey == null ? null : sessionKey.trim();
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Timestamp expiry) {
		this.expiry = expiry;
	}

	@Override
	public String toString() {
		return "SessionInfo [sessionId=" + sessionId + ", openId=" + openId + ", expiry=" + expiry + "]";
	}

}
