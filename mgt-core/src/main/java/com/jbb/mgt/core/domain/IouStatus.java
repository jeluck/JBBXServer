package com.jbb.mgt.core.domain;

public class IouStatus {
    private Integer status;
    private String iouCode;
    private String extentionDate;
    
    

    public IouStatus() {
        super();
    }

    public IouStatus( String iouCode, Integer status, String extentionDate) {
        super();
        this.status = status;
        this.iouCode = iouCode;
        this.extentionDate = extentionDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIouCode() {
        return iouCode;
    }

    public void setIouCode(String iouCode) {
        this.iouCode = iouCode;
    }

    public String getExtentionDate() {
        return extentionDate;
    }

    public void setExtentionDate(String extentionDate) {
        this.extentionDate = extentionDate;
    }
}
