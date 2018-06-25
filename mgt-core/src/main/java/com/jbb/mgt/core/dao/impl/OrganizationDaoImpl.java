package com.jbb.mgt.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.OrganizationDao;
import com.jbb.mgt.core.dao.mapper.OrganizationMapper;
import com.jbb.mgt.core.domain.Organization;

@Repository("OrganizationDao")
public class OrganizationDaoImpl implements OrganizationDao {

    @Autowired
    private OrganizationMapper mapper;

    @Override
    public void insertOrganization(Organization org) {
        mapper.insertOrganization(org);
    }

    @Override
    public List<Organization> getOrganizations() {
        return mapper.selectOrganizations();
    }

    @Override
    public Organization selectOrganizationById(int orgId) {

        return mapper.selectOrganizationById(orgId);
    }

    @Override
    public void updateOrganization(Organization org) {
        mapper.updateOrganization(org);
    }

    @Override
    public int deleteOrganization(int orgID) {

        return mapper.deleteOrganization(orgID);
    }

    @Override
    public List<Organization> getAllOrganizations() {
        return mapper.getAllOrganizations();
    }

}
