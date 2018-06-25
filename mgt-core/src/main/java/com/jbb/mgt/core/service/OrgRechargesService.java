package com.jbb.mgt.core.service;

import com.jbb.mgt.core.domain.OrgRecharges;

public interface OrgRechargesService {

    void insertOrgRecharges(OrgRecharges OrgRecharges);

    OrgRecharges selectOrgRecharges(int orgId);
    
    OrgRecharges selectOrgRechargesForUpdate(int orgId);

    void updateOrgRecharges(OrgRecharges OrgRecharges);

}
