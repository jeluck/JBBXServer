package com.jbb.mgt.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.dao.OrgRechargesDao;
import com.jbb.mgt.core.domain.Bill;
import com.jbb.mgt.core.domain.OrgRecharges;
import com.jbb.mgt.core.service.OrgRechargesService;

@Service("OrgRechargesService")
public class OrgRechargesServiceImpl implements OrgRechargesService {
    @Autowired
    private OrgRechargesDao orgRechargesDao;

    @Override
    public void insertOrgRecharges(OrgRecharges orgRecharges) {
        orgRechargesDao.insertOrgRecharges(orgRecharges);
    }

    @Override
    public OrgRecharges selectOrgRecharges(int orgId) {
        return orgRechargesDao.selectOrgRecharges(orgId);
    }

    @Override
    public void updateOrgRecharges(OrgRecharges OrgRecharges) {
        orgRechargesDao.updateOrgRecharges(OrgRecharges);     
    }

    @Override
    public OrgRecharges selectOrgRechargesForUpdate(int orgId) {
         return orgRechargesDao.selectOrgRechargesForUpdate(orgId);
    }

}
