package com.jbb.server.core.domain;

import java.sql.Timestamp;

/**
 * @author sunzhiying
 * @date 2018年5月9日
 */
public class UserDevice {

    private int userId;
    private String deviceType;
    private String objectId;
    private String installationId;
    private String deviceToken;
    private Timestamp creationDate;
    private Timestamp updateDate;
    private boolean status;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public boolean isIos() {
        return "ios".equals(this.deviceType);
    }

    public boolean isAndroid() {
        return "android".equals(this.deviceType);
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserDevice [userId=" + userId + ", deviceType=" + deviceType + ", objectId=" + objectId
            + ", installationId=" + installationId + ", deviceToken=" + deviceToken + ", creationDate=" + creationDate
            + ", updateDate=" + updateDate + ", status=" + status + "]";
    }

}
