package com.jbb.server.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LcMessageExchangeInfo {
    private int toClientId;
    private String phoneNumber;
    private String qqNumber;
    private String wechat;
    private UserBasic user;

    public LcMessageExchangeInfo() {

    }

    public LcMessageExchangeInfo(int toClientId) {
        this.toClientId = toClientId;
    }

    public int getToClientId() {
        return toClientId;
    }

    public void setToClientId(int toClientId) {
        this.toClientId = toClientId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getQqNumber() {
        return qqNumber;
    }

    public void setQqNumber(String qqNumber) {
        this.qqNumber = qqNumber;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public UserBasic getUser() {
        return user;
    }

    public void setUserBasic(User user) {
        this.user = new UserBasic(user);
    }

    @Override
    public String toString() {
        return "LcMessageExchangeInfo [toClientId=" + toClientId + ", phoneNumber=" + phoneNumber + ", qqNumber="
            + qqNumber + ", wechat=" + wechat + ", user=" + user + "]";
    }

}
