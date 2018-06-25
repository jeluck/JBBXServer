package com.jbb.mgt.core.domain;

public class ClubQueryReportResponse {
    
    public static int SUCCES_CODE = 0;
    
    private int code;
    private String message;
    private String data;
    private String originalData;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOriginalData() {
        return originalData;
    }

    public void setOriginalData(String originalData) {
        this.originalData = originalData;
    }

    @Override
    public String toString() {
        return "ClubQueryReportResponse [code=" + code + ", message=" + message + ", data=" + data + "]";
    }

}
