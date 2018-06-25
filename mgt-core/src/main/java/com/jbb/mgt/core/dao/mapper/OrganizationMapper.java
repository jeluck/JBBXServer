package com.jbb.mgt.core.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.Organization;

public interface OrganizationMapper {

    // 插入组织
    void insertOrganization(Organization org);

    // 获取组织列表
    List<Organization> selectOrganizations();

    List<Organization> getAllOrganizations();

    // 通过ID获取组织信息
    Organization selectOrganizationById(@Param(value = "orgId") int orgId);

    // 更新组织信息
    void updateOrganization(Organization org);

    // 删除组织信息
    int deleteOrganization(@Param(value = "orgId") int orgID);
}
