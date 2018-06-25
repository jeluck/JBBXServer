package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserApplyRecordsSpc {
    private Integer userId;
    private Integer accountId;
    private Timestamp creationDate;
    private User user;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserApplyRecordsSpc [userId=" + userId + ", accountId=" + accountId + ", creationDate=" + creationDate
            + ", user=" + user + "]";
    }

}
