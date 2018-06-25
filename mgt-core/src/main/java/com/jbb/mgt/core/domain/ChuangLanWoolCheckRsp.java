package com.jbb.mgt.core.domain;

public class ChuangLanWoolCheckRsp {

    public static String SUCCES_CODE = "200000";

    private String message;
    private String code;
    private ChuangLanWoolCheck data;

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

    public ChuangLanWoolCheck getData() {
        return data;
    }

    public void setData(ChuangLanWoolCheck data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ChuangLanWoolCheckRsp [message=" + message + ", code=" + code + ", data=" + data + "]";
    }

}
