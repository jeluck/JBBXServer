 package com.jbb.mgt.core.service;

import java.util.List;

import com.jbb.mgt.core.domain.RechargeRecord;

public interface RechargeRecordService {

     void insertRechargeRecord(RechargeRecord rechargeRecord);

     RechargeRecord selectRechargeRecord(String tradeNo);

     List<RechargeRecord> selectRechargeRecords(int orgId);
}
