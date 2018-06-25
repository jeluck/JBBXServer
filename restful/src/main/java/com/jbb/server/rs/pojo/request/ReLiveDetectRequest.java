package com.jbb.server.rs.pojo.request;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class ReLiveDetectRequest {
    private String validateData;
    private String fileName;
    private byte[] fileDate;

    public ReLiveDetectRequest() {}

    public String getValidateData() {
        return validateData;
    }

    @FormParam("validateData")
    public void setValidateData(String validateData) {
        this.validateData = validateData;
    }

    public String getFileName() {
        return fileName;
    }

    @FormParam("fileName")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileDate() {
        return fileDate;
    }

    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public void setFileDate(byte[] fileDate) {
        this.fileDate = fileDate;
    }
}