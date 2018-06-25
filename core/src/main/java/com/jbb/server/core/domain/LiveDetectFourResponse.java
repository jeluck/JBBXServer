package com.jbb.server.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LiveDetectFourResponse {

    public static int SUCCESS_CODE = 0;

    public static int SUCCESS_STATUS = 0;

    private int code;

    private String message;

    private CompareData data;

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

    public CompareData getData() {
        return data;
    }

    public void setData(CompareData data) {
        this.data = data;
    }

    public static class CompareData {
        private String photo;
        private int sim;
        @JsonProperty("live_status")
        private int liveStatus;
        @JsonProperty("live_msg")
        private String liveMsg;
        @JsonProperty("compare_status")
        private int compareStatus;
        @JsonProperty("compare_msg")
        private String compareMsg;

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public int getSim() {
            return sim;
        }

        public void setSim(int sim) {
            this.sim = sim;
        }

        public int getLiveStatus() {
            return liveStatus;
        }

        public void setLiveStatus(int liveStatus) {
            this.liveStatus = liveStatus;
        }

        public String getLiveMsg() {
            return liveMsg;
        }

        public void setLiveMsg(String liveMsg) {
            this.liveMsg = liveMsg;
        }

        public int getCompareStatus() {
            return compareStatus;
        }

        public void setCompareStatus(int compareStatus) {
            this.compareStatus = compareStatus;
        }

        public String getCompareMsg() {
            return compareMsg;
        }

        public void setCompareMsg(String compareMsg) {
            this.compareMsg = compareMsg;
        }

    }

}
