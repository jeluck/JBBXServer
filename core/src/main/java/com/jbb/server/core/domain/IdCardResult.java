package com.jbb.server.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdCardResult {

    public static final int SUCC_CODE = 0;
    @JsonProperty("error_code")
    private int errorCode;
    private String reason;
    private String ordersign;

    private Object result;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOrdersign() {
        return ordersign;
    }

    public void setOrdersign(String ordersign) {
        this.ordersign = ordersign;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public static int getSuccCode() {
        return SUCC_CODE;
    }

    @Override
    public String toString() {
        return "IdCardResult [errorCode=" + errorCode + ", reason=" + reason + ", ordersign=" + ordersign + ", result="
            + result + "]";
    }

}
