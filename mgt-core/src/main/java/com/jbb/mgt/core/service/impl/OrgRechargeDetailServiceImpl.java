package com.jbb.mgt.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.dao.AccountDao;
import com.jbb.mgt.core.dao.ChannelDao;
import com.jbb.mgt.core.dao.OrgRechargeDetailDao;
import com.jbb.mgt.core.domain.Account;
import com.jbb.mgt.core.domain.Channel;
import com.jbb.mgt.core.domain.OrgRechargeDetail;
import com.jbb.mgt.core.domain.TeleMarketing;
import com.jbb.mgt.core.service.OrgRechargeDetailService;
import com.jbb.mgt.server.core.util.StringUtils;
import com.jbb.server.common.exception.ObjectNotFoundException;
import com.jbb.server.common.util.DateUtil;

import net.sf.json.JSONObject;

@Service("OrgRechargeDetailService")
public class OrgRechargeDetailServiceImpl implements OrgRechargeDetailService {

    @Autowired
    private OrgRechargeDetailDao orgRechargeDetailDao;
    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private AccountDao accountDao;

    @Override
    public void insertOrgRechargeDetail(OrgRechargeDetail orgRechargeDetail) {
        orgRechargeDetailDao.insertOrgRechargeDetail(orgRechargeDetail);
    }

    @Override
    public OrgRechargeDetail selectOrgRechargeDetail(String tradeNo) {
        return orgRechargeDetailDao.selectOrgRechargeDetail(tradeNo);
    }

    @Override
    public List<OrgRechargeDetail> selectOrgRechargeDetailById(int accountId) {
        return orgRechargeDetailDao.selectOrgRechargeDetailById(accountId);
    }

    @Override
    public List<OrgRechargeDetail> selectAllOrgRechargeDetail(int orgId) {
        return orgRechargeDetailDao.selectAllOrgRechargeDetail(orgId);
    }

    @Override
    public void updateOrgRechargeDetail(OrgRechargeDetail orgRechargeDetail) {
        orgRechargeDetailDao.updateOrgRechargeDetail(orgRechargeDetail);
    }

    @Override
    public String handleConsumeSms(String channelCode, String phoneNumber, Integer smsPrice) {
        Channel channel = channelDao.selectChannelByCode(channelCode);
        if (null == channel) {
            throw new ObjectNotFoundException("jbb.mgt.exception.channel.notFound");
        }
        OrgRechargeDetail orgRechargeDetail = new OrgRechargeDetail();
        orgRechargeDetail.setAccountId(channel.getCreator());
        orgRechargeDetail.setAmount(smsPrice);
        orgRechargeDetail.setCreationDate(DateUtil.getCurrentTimeStamp());
        orgRechargeDetail.setOpType(22);
        orgRechargeDetail.setStatus(1);
        orgRechargeDetail.setTradeNo(StringUtils.getRandomString(32));

        Map<String, Object> mapInfo = new HashMap<>();
        mapInfo.put("phoneNumber", phoneNumber);
        mapInfo.put("sendDate", DateUtil.getCurrentTime());
        mapInfo.put("channelCode", channelCode);
        JSONObject jsonObj = JSONObject.fromObject(mapInfo);
        orgRechargeDetail.setParams(jsonObj.toString());
        orgRechargeDetailDao.insertOrgRechargeDetail(orgRechargeDetail);
        return orgRechargeDetail.getTradeNo();
    }

    @Override
    public String handleConsumeData(int orgId, int userId, int price) {
        OrgRechargeDetail orgRechargeDetail = new OrgRechargeDetail();
        Account orgAdmin = accountDao.selectOrgAdminAccount(orgId);
        orgRechargeDetail.setAccountId(orgAdmin.getAccountId());
        orgRechargeDetail.setAmount(price);
        orgRechargeDetail.setCreationDate(DateUtil.getCurrentTimeStamp());
        orgRechargeDetail.setOpType(22);
        orgRechargeDetail.setStatus(1);
        orgRechargeDetail.setTradeNo(StringUtils.getRandomString(32));

        Map<String, Object> mapInfo = new HashMap<>();
        mapInfo.put("orgId", orgId);
        mapInfo.put("userId", userId);
        JSONObject jsonObj = JSONObject.fromObject(mapInfo);
        orgRechargeDetail.setParams(jsonObj.toString());
        orgRechargeDetailDao.insertOrgRechargeDetail(orgRechargeDetail);
        return orgRechargeDetail.getTradeNo();
    }

    @Override
    public String consumePhoneNumberCheck(TeleMarketing teleMarketing) {
        return orgRechargeDetailDao.consumePhoneNumberCheck(teleMarketing);
    }

}
