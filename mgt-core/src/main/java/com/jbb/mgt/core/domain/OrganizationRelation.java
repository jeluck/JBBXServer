package com.jbb.mgt.core.domain;

public class OrganizationRelation {
    private Integer orgId;

    private Integer subOrgId;

    public OrganizationRelation(Integer orgId, Integer subOrgId) {
        this.orgId = orgId;
        this.subOrgId = subOrgId;
    }

    public OrganizationRelation() {
        super();
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getSubOrgId() {
        return subOrgId;
    }

    public void setSubOrgId(Integer subOrgId) {
        this.subOrgId = subOrgId;
    }
}