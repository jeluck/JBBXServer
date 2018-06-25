package com.jbb.server.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LenderReason {

    private int userId;
    private String reason;
    private String reasonDesc;
    private Integer point;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "LenderReason [userId=" + userId + ", reason=" + reason + ", reasonDesc=" + reasonDesc + ", point="
            + point + "]";
    }

    
}
