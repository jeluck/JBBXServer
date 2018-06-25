package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

/**
 * 二维码实体类
 *
 * @author wyq
 * @date 2018/6/1 11:55
 */
public class DataYxUrls {
    private int userId;
    private String reportType;
    private String h5Url;
    private Timestamp creationDate;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public DataYxUrls() {
        super();
    }

    public DataYxUrls(int userId, String reportType, String h5Url, Timestamp creationDate) {
        this.userId = userId;
        this.reportType = reportType;
        this.h5Url = h5Url;
        this.creationDate = creationDate;
    }
}
