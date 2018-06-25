package com.jbb.mgt.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YxNotify {

    public static String SUCCESS_TRUE ="true";
    
    private String taskId;
    private String token;
    private String orgId;
    private String sign;
    private String website;
    private String success;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

   

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "YxNotify [taskId=" + taskId + ", token=" + token + ", orgId=" + orgId + ", sign=" + sign + ", website="
            + website + ", success=" + success + "]";
    }

}
