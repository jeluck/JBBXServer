package com.jbb.server.core.domain;

import java.sql.Timestamp;

public class UserKey {
    // Application ID's
    public static final int APPLICATION_USER = 0;
    public static final int APPLICATION_TEMP_USER_KEY = 1;
    
    public static final String EMPTY_OAUTH_CLIENT_ID = "";

    private int userId;
    private String userKey;
    private Timestamp expiry;
    private boolean deleted;
    private int applicationId;
    private String oauthClientId;
    
    @Override
    public String toString() {
        return 
            "userId=" + userId +
            ", applicationId=" + applicationId +
            ", oauthClientId=" + oauthClientId +
            ", size=" + (userKey != null ? userKey.length() : 0) +
            ", expiry=" + expiry;
    }
    
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public String getUserKey() {
        return userKey;
    }
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
    public Timestamp getExpiry() {
        return expiry;
    }
    public void setExpiry(Timestamp expiry) {
        this.expiry = expiry;
    }
    public int getApplicationId() {
        return applicationId;
    }
    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getOauthClientId() {
        return oauthClientId;
    }
    public void setOauthClientId(String oauthClientId) {
        this.oauthClientId = oauthClientId;
    }
}
