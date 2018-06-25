package com.jbb.mgt.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Organization {

    private int orgId;
    private String name;
    private String description;
    private boolean deleted;
    private Integer count;
    private String smsSignName;
    private String smsTemplateId;
    private DataFlowSetting dataFlowSetting;
    private OrgRecharges recharge;
    private Statictic statictic;

    public Organization() {
        super();
    }

    public Organization(String name, String desc) {
        this.name = name;
        this.description = desc;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getSmsSignName() {
        return smsSignName;
    }

    public void setSmsSignName(String smsSignName) {
        this.smsSignName = smsSignName;
    }

    public String getSmsTemplateId() {
        return smsTemplateId;
    }

    public void setSmsTemplateId(String smsTemplateId) {
        this.smsTemplateId = smsTemplateId;
    }

    public OrgRecharges getRecharge() {
        return recharge;
    }

    public void setRecharge(OrgRecharges recharge) {
        this.recharge = recharge;
    }

    public Statictic getStatictic() {
        return statictic;
    }

    public void setStatictic(Statictic statictic) {
        this.statictic = statictic;
    }

    public DataFlowSetting getDataFlowSetting() {
        return dataFlowSetting;
    }

    public void setDataFlowSetting(DataFlowSetting dataFlowSetting) {
        this.dataFlowSetting = dataFlowSetting;
    }

    @Override
    public String toString() {
        return "Organization [orgId=" + orgId + ", name=" + name + ", description=" + description + ", deleted="
            + deleted + ", count=" + count + ", smsSignName=" + smsSignName + ", smsTemplateId=" + smsTemplateId
            + ", dataFlowSetting=" + dataFlowSetting + ", recharge=" + recharge + ", statictic=" + statictic + "]";
    }

}
