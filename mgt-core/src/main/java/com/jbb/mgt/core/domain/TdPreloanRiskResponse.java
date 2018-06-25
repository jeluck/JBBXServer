package com.jbb.mgt.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TdPreloanRiskResponse {

    private Boolean success;

    @JsonProperty("report_id")
    private String reportId;

    @JsonProperty("risk_items")
    private Object riskItems;

    @JsonProperty("address_detect")
    private Object addressDetect;

    @JsonProperty("application_id")
    private String applicationId;

    @JsonProperty("final_decision")
    private String finalDecision;

    @JsonProperty("apply_time")
    private Long applyTime;

    @JsonProperty("report_time")
    private Long reportTime;

    @JsonProperty("final_score")
    private Integer finalScore;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public Object getRiskItems() {
        return riskItems;
    }

    public void setRiskItems(Object riskItems) {
        this.riskItems = riskItems;
    }

    public Object getAddressDetect() {
        return addressDetect;
    }

    public void setAddressDetect(Object addressDetect) {
        this.addressDetect = addressDetect;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getFinalDecision() {
        return finalDecision;
    }

    public void setFinalDecision(String finalDecision) {
        this.finalDecision = finalDecision;
    }

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public Long getReportTime() {
        return reportTime;
    }

    public void setReportTime(Long reportTime) {
        this.reportTime = reportTime;
    }

    public Integer getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Integer finalScore) {
        this.finalScore = finalScore;
    }

}
