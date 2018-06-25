package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

public class AccountKey {

    private int accountId;
    private String userKey;
    private Timestamp expiry;
    private boolean deleted;

    @Override
    public String toString() {
        return "accountId=" + accountId + ", size=" + (userKey != null ? userKey.length() : 0) + ", expiry=" + expiry;
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

    public int getAccoountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

}
