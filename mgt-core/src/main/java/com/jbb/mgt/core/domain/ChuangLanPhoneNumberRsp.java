package com.jbb.mgt.core.domain;

public class ChuangLanPhoneNumberRsp {

    public static String SUCCES_CODE = "200000";
    private String message;
    private String code;
    private ChuangLanPhoneNumber data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ChuangLanPhoneNumber getData() {
        return data;
    }

    public void setData(ChuangLanPhoneNumber data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ChuangLanPhoneNumberRsp [message=" + message + ", code=" + code + ", data=" + data + "]";
    }

}
