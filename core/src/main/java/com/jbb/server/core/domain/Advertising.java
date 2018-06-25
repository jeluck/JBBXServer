package com.jbb.server.core.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbb.server.shared.rs.Util;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Advertising {
    private Integer adId;

    private String devicePlatform;

    private String advertisingName;

    private String picPath;

    private String content;

    private Integer priority;

    private String clickPath;

    @JsonIgnore
    private Timestamp creationDate;

    @JsonProperty("creationDate")
    private String creationDateStr;

    private Long creationDateMs;

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public String getDevicePlatform() {
        return devicePlatform;
    }

    public void setDevicePlatform(String devicePlatform) {
        this.devicePlatform = devicePlatform == null ? null : devicePlatform.trim();
    }

    public String getAdvertisingName() {
        return advertisingName;
    }

    public void setAdvertisingName(String advertisingName) {
        this.advertisingName = advertisingName == null ? null : advertisingName.trim();
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath == null ? null : picPath.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getClickPath() {
        return clickPath;
    }

    public void setClickPath(String clickPath) {
        this.clickPath = clickPath == null ? null : clickPath.trim();
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
        this.creationDateStr=Util.printDateTime(creationDate);
        this.creationDateMs=Util.getTimeMs(creationDate);
    }

    public Long getCreationDateMs() {
        return this.creationDateMs;
    }

    public String getCreationDateStr() {
        return this.creationDateStr;
    }

    @Override
    public String toString() {
        return "Advertising [adId=" + adId + ", devicePlatform=" + devicePlatform + ", advertisingName="
            + advertisingName + ", picPath=" + picPath + ", content=" + content + ", priority=" + priority
            + ", clickPath=" + clickPath + ", creationDate=" + creationDate + ", creationDateMs=" + creationDateMs
            + "]";
    }

}