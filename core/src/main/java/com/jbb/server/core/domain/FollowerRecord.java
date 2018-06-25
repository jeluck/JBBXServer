package com.jbb.server.core.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbb.server.shared.rs.Util;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FollowerRecord {

    private UserBasic user;

    private Iou iou;

    private String iouCode;
    private Integer status;
    private Integer userId;
    @JsonIgnore
    private Timestamp updateDate;
    @JsonProperty("updateDate")
    private String updateDateStr;
    private Long updateDateTs;

    public UserBasic getUser() {
        return user;
    }

    public void setUser(UserBasic user) {
        this.user = user;
    }

    public Iou getIou() {
        return iou;
    }

    public void setIou(Iou iou) {
        this.iou = iou;
    }

    public String getIouCode() {
        return iouCode;
    }

    public void setIouCode(String iouCode) {
        this.iouCode = iouCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
        this.updateDateStr = Util.printDateTime(updateDate);
        this.updateDateTs = Util.getTimeMs(updateDate);
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

    public Long getUpdateDateTs() {
        return updateDateTs;
    }

    public void setUpdateDateTs(Long updateDateTs) {
        this.updateDateTs = updateDateTs;
    }

    @Override
    public String toString() {
        return "IntendRecord [user=" + user + ", iou=" + iou + ", iouCode=" + iouCode + ", status=" + status
            + ", userId=" + userId + ", updateDate=" + updateDate + ", updateDateStr=" + updateDateStr
            + ", updateDateTs=" + updateDateTs + "]";
    }

}
