package com.jbb.mgt.core.domain;

public class UserResponse {

    public static int SUCCES_CODE = 0;
    private int resultCode;
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

}
