package com.jbb.mgt.core.service;

import java.util.List;

import com.jbb.mgt.core.domain.Organization;

public interface OrganizationService {
    // 插入组织
    void insertOrganization(Organization org);

    // 获取组织列表
    List<Organization> getOrganizations();

    List<Organization> getAllOrganizations(Boolean getStatistic);

    // 通过ID获取组织信息
    Organization getOrganizationById(int orgId);

    // 更新组织信息
    void updateOrganization(Organization org);

    // 删除组织信息
    boolean deleteOrganization(int orgId);

}
