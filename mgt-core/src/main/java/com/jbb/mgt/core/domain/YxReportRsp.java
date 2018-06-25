package com.jbb.mgt.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YxReportRsp {

    private boolean success;
    private String code;
    private String msg;
    private Object detail;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getDetail() {
        return detail;
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "YxReportRsp [success=" + success + ", code=" + code + ", msg=" + msg + ", detail=" + detail + "]";
    }

}
