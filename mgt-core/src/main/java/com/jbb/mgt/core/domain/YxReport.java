package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

import com.jbb.server.common.util.DateUtil;

public class YxReport {

    public static int STATUS_INIT = 0;
    public static int STATUS_COMPLATE = 99;
    public static int STATUS_ERROR = 1;

    public static final String SPLIT_TASK_ID_CHAR = "_";
    // 借贷宝 10、米房 11、无忧12，今借到13 借条数据。
    // 2位编号_四位字母数字随机串_20180503152012(年月日时分秒)_userId_applyId
    public static final String TYPE_JIEDAIBAO_NUM = "10";
    public static final String TYPE_MIFANG_NUM = "11";
    public static final String TYPE_WUYOU_NUM = "12";
    public static final String TYPE_JINJIEDAO_NUM = "13";

    // jiedaibao - 借贷宝，mifang - 米房， wuyoujietiao- 无忧，jinjiedao -今借到
    public static final String TYPE_JIEDAIBAO = "jiedaibao";
    public static final String TYPE_MIFANG = "mifang";
    public static final String TYPE_WUYOU = "wuyoujietiao";
    public static final String TYPE_JINJIEDAO = "jinjiedao";

    private String taskId;
    private int userId;
    private int applyId;
    private String reportType;
    private String token;
    private int status;
    private Timestamp creationDate;

    private String data;

    public YxReport() {

    }

    public YxReport(String taskId, int userId, int applyId, String reportType, String token) {
        super();
        this.taskId = taskId;
        this.userId = userId;
        this.applyId = applyId;
        this.reportType = reportType;
        this.token = token;
        this.status = STATUS_INIT;
        this.creationDate = DateUtil.getCurrentTimeStamp();
    }

    public YxReport(YxNotify notify) {
        this.taskId = notify.getTaskId();
        String[] arr = this.taskId.split(SPLIT_TASK_ID_CHAR);
        this.setUserId(Integer.valueOf(arr[3]));
        this.setApplyId(Integer.valueOf(arr[4]));
        this.setReportType(getReportTypeFromNum(arr[0]));
        this.setToken(notify.getToken());
        this.status = STATUS_INIT;
        this.creationDate = DateUtil.getCurrentTimeStamp();
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getReportTypeFromNum(String type) {
        String typeStr = null;
        switch (type) {
            case TYPE_JIEDAIBAO_NUM:
                typeStr = TYPE_JIEDAIBAO;
                break;
            case TYPE_MIFANG_NUM:
                typeStr = TYPE_MIFANG;
                break;
            case TYPE_WUYOU_NUM:
                typeStr = TYPE_WUYOU;
                break;
            case TYPE_JINJIEDAO_NUM:
                typeStr = TYPE_JINJIEDAO;
                break;
        }
        return typeStr;
    }

    @Override
    public String toString() {
        return "YxReport [taskId=" + taskId + ", userId=" + userId + ", applyId=" + applyId + ", reportType="
            + reportType + ", token=" + token + ", status=" + status + ", creationDate=" + creationDate + "]";
    }

}
