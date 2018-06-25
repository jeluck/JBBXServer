package com.jbb.mgt.core.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.OrgRechargeDetailDao;
import com.jbb.mgt.core.dao.mapper.OrgRechargeDetailMapper;
import com.jbb.mgt.core.domain.OrgRechargeDetail;
import com.jbb.mgt.core.domain.TeleMarketing;
import com.jbb.mgt.server.core.util.StringUtils;
import com.jbb.server.common.exception.ObjectNotFoundException;
import com.jbb.server.common.util.DateUtil;

import net.sf.json.JSONObject;

@Repository("OrgRechargeDetailDao")
public class OrgRechargeDetailDaoImpl implements OrgRechargeDetailDao {
    @Autowired
    private OrgRechargeDetailMapper mapper;

    @Override
    public void insertOrgRechargeDetail(OrgRechargeDetail orgRechargeDetail) {
        mapper.insertOrgRechargeDetail(orgRechargeDetail);
    }

    @Override
    public OrgRechargeDetail selectOrgRechargeDetail(String tradeNo) {
        return mapper.selectOrgRechargeDetail(tradeNo);
    }

    @Override
    public List<OrgRechargeDetail> selectAllOrgRechargeDetail(int orgId) {
        return mapper.selectAllOrgRechargeDetail(orgId);
    }

    @Override
    public void updateOrgRechargeDetail(OrgRechargeDetail orgRechargeDetail) {
        mapper.updateOrgRechargeDetail(orgRechargeDetail);
    }

    @Override
    public List<OrgRechargeDetail> selectOrgRechargeDetailById(int accountId) {
         return mapper.selectOrgRechargeDetailById(accountId);
    }

    @Override
    public String consumePhoneNumberCheck(TeleMarketing teleMarketing) {
            if(teleMarketing==null) {
                throw new ObjectNotFoundException("jbb.mgt.exception.teleMarketing.notFound"); 
            }
            OrgRechargeDetail orgRechargeDetail = new OrgRechargeDetail();
            orgRechargeDetail.setAccountId(teleMarketing.getAccountId());
            orgRechargeDetail.setAmount(teleMarketing.getPrice());
            orgRechargeDetail.setCreationDate(DateUtil.getCurrentTimeStamp());
            orgRechargeDetail.setOpType(22);
            orgRechargeDetail.setStatus(1);
            orgRechargeDetail.setTradeNo(StringUtils.getRandomString(32));

            Map<String, Object> mapInfo = new HashMap<>();
            mapInfo.put("batchId", teleMarketing.getBatchId());
            mapInfo.put("checkDate", DateUtil.getCurrentTime());
            mapInfo.put("price", teleMarketing.getPrice());
            JSONObject jsonObj = JSONObject.fromObject(mapInfo);
            orgRechargeDetail.setParams(jsonObj.toString());
            mapper.insertOrgRechargeDetail(orgRechargeDetail);
            return orgRechargeDetail.getTradeNo();
    }

}
