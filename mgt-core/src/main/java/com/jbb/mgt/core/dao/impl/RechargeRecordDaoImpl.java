package com.jbb.mgt.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.RechargeRecordDao;
import com.jbb.mgt.core.dao.mapper.RechargeRecordMapper;
import com.jbb.mgt.core.domain.RechargeRecord;

@Repository("RechargeRecordDao")
public class RechargeRecordDaoImpl implements RechargeRecordDao {

    @Autowired
    private RechargeRecordMapper mapper;

    @Override
    public void insertRechargeRecord(RechargeRecord rechargeRecord) {
        mapper.insertRechargeRecord(rechargeRecord);
    }

    @Override
    public RechargeRecord selectRechargeRecord(String tradeNo) {
        return mapper.selectRechargeRecord(tradeNo);
    }

    @Override
    public List<RechargeRecord> selectRechargeRecords(int orgId) {
        return mapper.selectRechargeRecords(orgId);
    }

}
