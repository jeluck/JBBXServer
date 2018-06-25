package com.jbb.server.core.doman.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LeanCloudPayload {

    private Object data;

    private String prod;

    private String cql;
    @JsonProperty("expiration_interval")
    private String expirationInterval;
    @JsonProperty("expiration_time")
    private String expirationTime;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCql() {
        return cql;
    }

    public void setCql(String cql) {
        this.cql = cql;
    }

    public String getExpirationInterval() {
        return expirationInterval;
    }

    public void setExpirationInterval(String expirationInterval) {
        this.expirationInterval = expirationInterval;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public static class IosAlertData {

        private String alert;
        private String category;
        private String badge;

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getBadge() {
            return badge;
        }

        public void setBadge(String badge) {
            this.badge = badge;
        }

        @Override
        public String toString() {
            return "IosAlertData [alert=" + alert + ", category=" + category + ", badge=" + badge + "]";
        }

    }

    public static class AndroidAlertData {

        private String alert;
        private String title;

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "AndroidAlertData [alert=" + alert + ", title=" + title + "]";
        }

    }

}
