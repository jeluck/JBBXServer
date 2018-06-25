package com.jbb.mgt.core.domain;

public class IouPlatform {
    private Integer iouPlatformId;
    private String platformName;
    private String description;

    public Integer getIouPlatformId() {
        return iouPlatformId;
    }

    public void setIouPlatformId(Integer iouPlatformId) {
        this.iouPlatformId = iouPlatformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "IouPlatform [iouPlatformId=" + iouPlatformId + ", platformName=" + platformName + ", description="
            + description + "]";
    }

}
