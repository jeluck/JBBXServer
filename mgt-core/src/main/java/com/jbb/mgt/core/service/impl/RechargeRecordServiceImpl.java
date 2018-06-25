package com.jbb.mgt.core.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.dao.RechargeRecordDao;
import com.jbb.mgt.core.domain.RechargeRecord;
import com.jbb.mgt.core.service.RechargeRecordService;

@Service("RechargeRecordService")
public class RechargeRecordServiceImpl implements RechargeRecordService {
    private static Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);
    @Autowired
    private RechargeRecordDao rechargeRecordDao;

    @Override
    public void insertRechargeRecord(RechargeRecord rechargeRecord) {
        rechargeRecordDao.insertRechargeRecord(rechargeRecord);
    }

    @Override
    public RechargeRecord selectRechargeRecord(String tradeNo) {
        return rechargeRecordDao.selectRechargeRecord(tradeNo);
    }

    @Override
    public List<RechargeRecord> selectRechargeRecords(int orgId) {
        return rechargeRecordDao.selectRechargeRecords(orgId);
    }

}
