package com.jbb.mgt.core.service.impl;

import com.jbb.mgt.core.dao.OrganizationRelationDao;
import com.jbb.mgt.core.domain.OrganizationRelation;
import com.jbb.mgt.core.service.OrganizationRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("OrganizationRelationService")
public class OrganizationRelationServiceImpl implements OrganizationRelationService {

    @Autowired
    OrganizationRelationDao organizationRelationDao;

    @Override
    public int deleteByPrimaryKey(OrganizationRelation record) {
        return organizationRelationDao.deleteByPrimaryKey(record);
    }

    @Override
    public void insert(OrganizationRelation record) {
        organizationRelationDao.insert(record);
    }

    @Override
    public void insertSelective(OrganizationRelation record) {
        organizationRelationDao.insertSelective(record);
    }

    @Override
    public List<OrganizationRelation> selectOrgRelationByOrgId(Integer orgId) {
        return organizationRelationDao.selectOrgRelationByOrgId(orgId);
    }
}
