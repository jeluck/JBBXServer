package com.jbb.server.core.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbb.server.shared.rs.Util;

public class IouIntention {

    private int userId;
    private String iouCode;
    private int status;

    @JsonIgnore
    private Timestamp updateDate;
    @JsonProperty("updateDateStr")
    private String updateDateStr;
    private Long updateDateTs;

    public IouIntention() {}

    public IouIntention(int userId, String iouCode, int status) {
        this.userId = userId;
        this.iouCode = iouCode;
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getIouCode() {
        return iouCode;
    }

    public void setIouCode(String iouCode) {
        this.iouCode = iouCode;
    }

    /**
     * 意向状态：0有意向，1 被拒绝，2自己取消意向
     * 
     * @return
     */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
        this.updateDateStr=Util.printDateTime(this.updateDate);
        this.updateDateTs= Util.getTimeMs(this.updateDate);
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
        return "IntentionRecord [userId=" + userId + ", iouCode=" + iouCode + ", status=" + status + ", updateDate="
            + updateDate + "]";
    }
}
