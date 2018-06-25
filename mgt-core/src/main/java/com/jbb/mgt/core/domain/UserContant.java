package com.jbb.mgt.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserContant {
    private Integer userId;
    private String phoneNumber;
    private String userName;
    private String vcard;
    private String jsonStr;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVcard() {
        return vcard;
    }

    public void setVcard(String vcard) {
        this.vcard = vcard;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public UserContant(Integer userId, String phoneNumber, String userName, String vcard) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.vcard = vcard;
    }

    public UserContant() {
    }
}
