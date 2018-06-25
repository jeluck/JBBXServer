 package com.jbb.mgt.core.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.OrgRechargesDao;
import com.jbb.mgt.core.dao.mapper.OrgRechargesMapper;
import com.jbb.mgt.core.domain.Bill;
import com.jbb.mgt.core.domain.OrgRecharges;
@Repository("OrgRechargesDao")
public class OrgRechargesDaoImpl implements OrgRechargesDao {

    @Autowired
    private OrgRechargesMapper mapper;
    @Override
    public void insertOrgRecharges(OrgRecharges orgRecharges) {
        mapper.insertOrgRecharges(orgRecharges);
    }

    @Override
    public OrgRecharges selectOrgRecharges(int orgId) {
        return mapper.selectOrgRecharges(orgId);
    }

    @Override
    public void updateOrgRecharges(OrgRecharges orgRecharges) {
        mapper. updateOrgRecharges(orgRecharges);
    }

    @Override
    public OrgRecharges selectOrgRechargesForUpdate(int orgId) {
         return mapper.selectOrgRechargesForUpdate(orgId);
    }

}
