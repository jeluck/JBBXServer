package com.jbb.mgt.core.domain;

public class LoanRecordUpdateRsp {
    public static String SUCCES_CODE = "0";

    private String resultCode;
    private boolean success;
    private String resultCodeMessage;

    public String getResultCodeMessage() {
        return resultCodeMessage;
    }

    public void setResultCodeMessage(String resultCodeMessage) {
        this.resultCodeMessage = resultCodeMessage;
    }

    public static String getSuccesCode() {
        return SUCCES_CODE;
    }

    public static void setSuccesCode(String succesCode) {
        SUCCES_CODE = succesCode;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
