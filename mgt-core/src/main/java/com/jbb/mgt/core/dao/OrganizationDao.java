package com.jbb.mgt.core.dao;

import java.util.List;

import com.jbb.mgt.core.domain.Organization;

public interface OrganizationDao {

    // 插入组织
    void insertOrganization(Organization org);

    // 获取组织列表
    List<Organization> getOrganizations();

    // 通过ID获取组织信息
    Organization selectOrganizationById(int orgId);

    // 更新组织信息
    void updateOrganization(Organization org);

    // 删除组织信息
   int deleteOrganization(int orgID);

    List<Organization> getAllOrganizations();
}
