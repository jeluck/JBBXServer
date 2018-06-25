package com.jbb.server.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LiveGetFourResponse {

    public static int SUCCESS_CODE = 0;

    public static int SUCCESS_STATUS = 0;

    private int code;

    private String message;

    private ValidateData data;

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

    public ValidateData getData() {
        return data;
    }

    public void setData(ValidateData data) {
        this.data = data;
    }

    public static class ValidateData {

        @JsonProperty("validate_data")
        private String validateData;

        public String getValidateData() {
            return validateData;
        }

        public void setValidateData(String validateData) {
            this.validateData = validateData;
        }

    }

    @Override
    public String toString() {
        return "LiveGetFourResponse [code=" + code + ", message=" + message + ", data=" + data + "]";
    }

}
