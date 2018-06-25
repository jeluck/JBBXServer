package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

public class PreloanReport {

    public static final int STATUS_SUCC = 99;

    private String reportId;
    private int userId;
    private int applyId;
    private String req;
    private String rsp;
    private Timestamp applyDate;
    private Timestamp reportDate;
    private int finalScore;
    private String finalDecision;
    private int status;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getApplyId() {
        return applyId;
    }

    public void setApplyId(int applyId) {
        this.applyId = applyId;
    }

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }

    public String getRsp() {
        return rsp;
    }

    public void setRsp(String rsp) {
        this.rsp = rsp;
    }

    public Timestamp getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Timestamp applyDate) {
        this.applyDate = applyDate;
    }

    public Timestamp getReportDate() {
        return reportDate;
    }

    public void setReportDate(Timestamp reportDate) {
        this.reportDate = reportDate;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    public String getFinalDecision() {
        return finalDecision;
    }

    public void setFinalDecision(String finalDecision) {
        this.finalDecision = finalDecision;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PreloanReport [reportId=" + reportId + ", userId=" + userId + ", applyId=" + applyId + ", req=" + req
            + ", rsp=" + rsp + ", applyDate=" + applyDate + ", reportDate=" + reportDate + ", finalScore=" + finalScore
            + ", finalDecision=" + finalDecision + ", status=" + status + "]";
    }

}
