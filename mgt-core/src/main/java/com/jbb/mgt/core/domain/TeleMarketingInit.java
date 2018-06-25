package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

public class TeleMarketingInit {

    private int id;
    private int assignAccountId;
    private int initAccountId;
    private String opCommet;
    private boolean opCommetFlag;
    private Timestamp assignDate;
    private Timestamp updateDate;

    TeleMarketingDetail mobilDetail;
    TeleMarketing batchInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssignAccountId() {
        return assignAccountId;
    }

    public void setAssignAccountId(int assignAccountId) {
        this.assignAccountId = assignAccountId;
    }

    public int getInitAccountId() {
        return initAccountId;
    }

    public void setInitAccountId(int initAccountId) {
        this.initAccountId = initAccountId;
    }

    public String getOpCommet() {
        return opCommet;
    }

    public void setOpCommet(String opCommet) {
        this.opCommet = opCommet;
    }

    public boolean isOpCommetFlag() {
        return opCommetFlag;
    }

    public void setOpCommetFlag(boolean opCommetFlag) {
        this.opCommetFlag = opCommetFlag;
    }

    public Timestamp getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(Timestamp assignDate) {
        this.assignDate = assignDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public TeleMarketingDetail getMobilDetail() {
        return mobilDetail;
    }

    public void setMobilDetail(TeleMarketingDetail mobilDetail) {
        this.mobilDetail = mobilDetail;
    }

    public TeleMarketing getBatchInfo() {
        return batchInfo;
    }

    public void setBatchInfo(TeleMarketing batchInfo) {
        this.batchInfo = batchInfo;
    }

    public TeleMarketingInit(int id, int assignAccountId, int initAccountId, String opCommet, boolean opCommetFlag,
        Timestamp assignDate, Timestamp updateDate, TeleMarketingDetail mobilDetail, TeleMarketing batchInfo) {
        super();
        this.id = id;
        this.assignAccountId = assignAccountId;
        this.initAccountId = initAccountId;
        this.opCommet = opCommet;
        this.opCommetFlag = opCommetFlag;
        this.assignDate = assignDate;
        this.updateDate = updateDate;
        this.mobilDetail = mobilDetail;
        this.batchInfo = batchInfo;
    }

    public TeleMarketingInit() {
        super();
    }

    @Override
    public String toString() {
        return "TeleMarketingInit [id=" + id + ", assignAccountId=" + assignAccountId + ", initAccountId="
            + initAccountId + ", opCommet=" + opCommet + ", opCommetFlag=" + opCommetFlag + ", assignDate=" + assignDate
            + ", updateDate=" + updateDate + ", mobilDetail=" + mobilDetail + ", batchInfo=" + batchInfo + "]";
    }

}
