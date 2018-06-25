package com.jbb.mgt.core.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.RechargeRecord;

public interface RechargeRecordMapper {

    void insertRechargeRecord(RechargeRecord rechargeRecord);

    RechargeRecord selectRechargeRecord(@Param(value = "tradeNo") String trade_no);

    List<RechargeRecord> selectRechargeRecords(@Param(value = "orgId") int orgId);

}
