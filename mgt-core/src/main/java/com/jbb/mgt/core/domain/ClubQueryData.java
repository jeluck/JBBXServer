package com.jbb.mgt.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClubQueryData {
    @JsonProperty("channel_type")
    private String channelType;
    @JsonProperty("channel_code")
    private String channelCode;
    @JsonProperty("channel_src")
    private String channelSrc;
    @JsonProperty("channel_attr")
    private String channelAttr;
    @JsonProperty("real_name")
    private String realName;
    @JsonProperty("identity_code")
    private String identityCode;
    @JsonProperty("user_mobile")
    private String userMobile;
    @JsonProperty("created_time")
    private String createdTime;
    @JsonProperty("user_name")
    private String username;
    @JsonProperty("task_data")
    private Object taskData;
    @JsonProperty("lost_data")
    private Object lostData;

    @JsonProperty("task_id")
    private String taskId;

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelSrc() {
        return channelSrc;
    }

    public void setChannelSrc(String channelSrc) {
        this.channelSrc = channelSrc;
    }

    public String getChannelAttr() {
        return channelAttr;
    }

    public void setChannelAttr(String channelAttr) {
        this.channelAttr = channelAttr;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Object getTaskData() {
        return taskData;
    }

    public void setTaskData(Object taskData) {
        this.taskData = taskData;
    }

    public Object getLostData() {
        return lostData;
    }

    public void setLostData(Object lostData) {
        this.lostData = lostData;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "ClubQueryData [channelType=" + channelType + ", channelCode=" + channelCode + ", channelSrc="
            + channelSrc + ", channelAttr=" + channelAttr + ", realName=" + realName + ", identityCode=" + identityCode
            + ", userMobile=" + userMobile + ", createdTime=" + createdTime + ", username=" + username + ", taskData="
            + taskData + ", lostData=" + lostData + "]";
    }

}
