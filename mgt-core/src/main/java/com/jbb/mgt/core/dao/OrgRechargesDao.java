package com.jbb.mgt.core.dao;

import com.jbb.mgt.core.domain.Bill;
import com.jbb.mgt.core.domain.OrgRecharges;

public interface OrgRechargesDao {

    void insertOrgRecharges(OrgRecharges orgRecharges);

    OrgRecharges selectOrgRecharges(int orgId);

    void updateOrgRecharges(OrgRecharges orgRecharges);

    OrgRecharges selectOrgRechargesForUpdate(int orgId);
    
}
