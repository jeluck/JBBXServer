package com.jbb.mgt.core.service;

import com.jbb.mgt.core.domain.OrganizationRelation;

import java.util.List;

public interface OrganizationRelationService {
    int deleteByPrimaryKey(OrganizationRelation record);

    void insert(OrganizationRelation record);

    void insertSelective(OrganizationRelation record);

    List<OrganizationRelation> selectOrgRelationByOrgId(Integer orgId);
}
