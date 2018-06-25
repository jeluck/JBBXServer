package com.jbb.mgt.server.rs.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.Organization;

/**
 * 组织 response对象
 * 
 * @author wyq
 * @date 2018/6/6 17:10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsOrganization {
    private int orgId;
    private String name;
    private String description;
    private boolean deleted;
    private Integer count;
    private String smsSignName;
    private String smsTemplateId;

    public RsOrganization() {
        super();
    }

    public RsOrganization(Organization organization) {
        this.orgId = organization.getOrgId();
        this.name = organization.getName();
        this.description = organization.getDescription();
        this.deleted = organization.isDeleted();
        this.count = organization.getCount();
        this.smsSignName = organization.getSmsSignName();
        this.smsTemplateId = organization.getSmsTemplateId();
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
}
