 package com.jbb.mgt.core.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.Bill;
import com.jbb.mgt.core.domain.OrgRecharges;

public interface OrgRechargesMapper {

    void insertOrgRecharges(OrgRecharges orgRecharges);

    OrgRecharges selectOrgRecharges(@Param(value = "orgId") int orgId);

    void updateOrgRecharges(OrgRecharges orgRecharges);

    OrgRecharges selectOrgRechargesForUpdate(@Param(value = "orgId") int orgId);
    
}
