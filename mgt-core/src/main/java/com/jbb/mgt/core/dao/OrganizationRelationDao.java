package com.jbb.mgt.core.dao;

import com.jbb.mgt.core.domain.OrganizationRelation;

import java.util.List;

public interface OrganizationRelationDao {
    int deleteByPrimaryKey(OrganizationRelation record);

    void insert(OrganizationRelation record);

    void insertSelective(OrganizationRelation record);

    List<OrganizationRelation> selectOrgRelationByOrgId(Integer orgId);
}
