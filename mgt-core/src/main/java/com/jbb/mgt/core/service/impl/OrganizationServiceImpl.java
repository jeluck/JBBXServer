package com.jbb.mgt.core.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.dao.OrganizationDao;
import com.jbb.mgt.core.dao.UserApplyRecordDao;
import com.jbb.mgt.core.domain.Organization;
import com.jbb.mgt.core.domain.Statictic;
import com.jbb.mgt.core.service.OrganizationService;
import com.jbb.server.common.util.DateUtil;

@Service("OrganizationService")
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private UserApplyRecordDao userApplyRecordDao;

    @Override
    public void insertOrganization(Organization org) {
        organizationDao.insertOrganization(org);
    }

    @Override
    public List<Organization> getOrganizations() {
        return organizationDao.getOrganizations();
    }

    @Override
    public Organization getOrganizationById(int orgId) {
        return organizationDao.selectOrganizationById(orgId);
    }

    @Override
    public void updateOrganization(Organization org) {
        organizationDao.updateOrganization(org);

    }

    @Override
    public boolean deleteOrganization(int orgID) {
        return organizationDao.deleteOrganization(orgID) > 1;
    }

    @Override
    public List<Organization> getAllOrganizations(Boolean getStatistic) {
        List<Organization> l = organizationDao.getAllOrganizations();
        long ts = DateUtil.getTodayStartCurrentTime();
        long ts1 = ts - 1 * DateUtil.DAY_MILLSECONDES;
        long ts2 = ts - 2 * DateUtil.DAY_MILLSECONDES;
        long ts3 = ts + 1 * DateUtil.DAY_MILLSECONDES;
        if (getStatistic && null != l && l.size() != 0) {
            for (int i = 0; i < l.size(); i++) {
                Integer orgId = l.get(i).getOrgId();
                Statictic statictic = new Statictic();
                statictic.setToday(userApplyRecordDao.countUserApply(orgId, new Timestamp(ts), new Timestamp(ts3)));
                statictic.setYesterday(userApplyRecordDao.countUserApply(orgId, new Timestamp(ts1), new Timestamp(ts)));
                statictic.setTheDayBeforeYesterday(
                    userApplyRecordDao.countUserApply(orgId, new Timestamp(ts2), new Timestamp(ts1)));
                l.get(i).setStatictic(statictic);
            }
        }
        return l;
    }

}
