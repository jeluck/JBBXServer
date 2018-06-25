package com.jbb.mgt.server.rs.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.TeleMarketingInit;
import com.jbb.server.shared.rs.Util;

/**
 * 批次明细和init表
 *
 * @author wyq
 * @date 2018/5/19 09:52
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class InitOp {
    private int id;
    private int batchId;
    private String mobile;
    private String username;
    private String area;
    private String numberType;
    private String chargesStatus;
    private Long lastTime;
    private int status;
    private Long updateDate;
    private int assignAccountId;
    private int initAccountId;
    private String opCommet;
    private boolean opCommetFlag;
    private Long assignDate;

    public InitOp(TeleMarketingInit init) {
        this.id = init.getId();
        this.batchId = init.getMobilDetail().getBatchId();
        this.mobile = init.getMobilDetail().getTelephone();
        this.username = init.getMobilDetail().getUsername();
        this.area = init.getMobilDetail().getArea();
        this.numberType = init.getMobilDetail().getNumberType();
        this.chargesStatus = init.getMobilDetail().getChargesStatus();
        this.lastTime = Util.getTimeMs(init.getMobilDetail().getLastDate());
        this.status = init.getMobilDetail().getStatus();
        this.updateDate = Util.getTimeMs(init.getMobilDetail().getUpdateDate());
        this.assignAccountId = init.getAssignAccountId();
        this.initAccountId = init.getInitAccountId();
        this.opCommet = init.getOpCommet();
        this.opCommetFlag = init.isOpCommetFlag();
        this.assignDate = Util.getTimeMs(init.getAssignDate());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }

    public String getChargesStatus() {
        return chargesStatus;
    }

    public void setChargesStatus(String chargesStatus) {
        this.chargesStatus = chargesStatus;
    }

    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
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

    public Long getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(Long assignDate) {
        this.assignDate = assignDate;
    }
}
