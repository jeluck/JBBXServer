package com.jbb.mgt.core.domain;

public class ClubReport {
    
    
    public static final String TYPE_YYS ="YYS";

    public static final int STATUS_SUCC = 99;
    public static final int STATUS_CREATED = 1;
    public static final int STATUS_TIMEOUT = 2;
    public static final int STATUS_FAILURE = 3;

    private String taskId;
    private Integer userId;
    private String channelType;
    private String channelCode;
    private String channelSrc;
    private String channelAttr;
    private String realName;
    private String identityCode;
    private String createdTime;
    private String username;
    private int status;
    private String data;

    private String report;

    public ClubReport() {

    }

    public ClubReport(String taskId, int status, ClubQueryData queryData) {
        this.taskId = taskId;
        this.channelAttr = queryData.getChannelAttr();
        this.channelCode = queryData.getChannelCode();
        this.channelSrc = queryData.getChannelSrc();
        this.channelType = queryData.getChannelType();
        this.realName = queryData.getRealName();
        this.identityCode = queryData.getIdentityCode();
        this.username = queryData.getUsername();
        this.createdTime = queryData.getCreatedTime();
        this.status = status;

    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    @Override
    public String toString() {
        return "ClubReport [taskId=" + taskId + ", channelType=" + channelType + ", channelCode=" + channelCode
            + ", channelSrc=" + channelSrc + ", channelAttr=" + channelAttr + ", realName=" + realName
            + ", identityCode=" + identityCode + ", createdTime=" + createdTime + ", username=" + username + ", status="
            + status + ", data=" + data + "]";
    }

}
