package com.jbb.mgt.core.dao;

import java.util.List;

import com.jbb.mgt.core.domain.RechargeRecord;

public interface RechargeRecordDao {

    void insertRechargeRecord(RechargeRecord rechargeRecord);

    RechargeRecord selectRechargeRecord(String tradeNo);

    List<RechargeRecord> selectRechargeRecords(int orgId);
}
