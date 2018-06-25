package com.jbb.mgt.core.dao;

import java.util.List;

import com.jbb.mgt.core.domain.OrgRechargeDetail;
import com.jbb.mgt.core.domain.TeleMarketing;

public interface OrgRechargeDetailDao {

    void insertOrgRechargeDetail(OrgRechargeDetail orgRechargeDetail);

    OrgRechargeDetail selectOrgRechargeDetail(String tradeNo);
    
    List<OrgRechargeDetail> selectOrgRechargeDetailById(int accountId);
    
    List<OrgRechargeDetail> selectAllOrgRechargeDetail(int orgId);

    void updateOrgRechargeDetail(OrgRechargeDetail orgRechargeDetail);

    String consumePhoneNumberCheck(TeleMarketing teleMarketing);
}
