package com.jbb.mgt.core.dao.impl;

import com.jbb.mgt.core.dao.OrganizationRelationDao;
import com.jbb.mgt.core.dao.mapper.OrganizationRelationMapper;
import com.jbb.mgt.core.domain.OrganizationRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("OrganizationRelationDao")
public class OrganizationRelationDaoImpl implements OrganizationRelationDao{

    @Autowired
    OrganizationRelationMapper mapper;

    @Override
    public int deleteByPrimaryKey(OrganizationRelation record) {
        return mapper.deleteByPrimaryKey(record);
    }

    @Override
    public void insert(OrganizationRelation record) {
        mapper.insert(record);
    }

    @Override
    public void insertSelective(OrganizationRelation record) {
        mapper.insertSelective(record);
    }

    @Override
    public List<OrganizationRelation> selectOrgRelationByOrgId(Integer orgId) {
        return mapper.selectOrgRelationByOrgId(orgId);
    }
}
