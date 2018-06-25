package com.jbb.mgt.core.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.TeleMarketingDetail;

/**
 * 批次明细数据Dao接口
 * 
 * @author wyq
 * @date 2018/04/29
 */
public interface TeleMarketingDetailDao {

    List<TeleMarketingDetail> selectMaxTeleMarketingDetail(int accountId);

    List<TeleMarketingDetail> selectTeleMarketingDetails(Integer betchId);

    int insertMobiles(@Param(value = "batchId") int batchId, List<TeleMarketingDetail> mobiles);

    boolean updateMobile(TeleMarketingDetail mobile);

    List<TeleMarketingDetail> selectTeleMarketingDetailsNotInit(int batchId, boolean test);

    List<TeleMarketingDetail> selectTeleMarketingByStatus(int status, int limit, int batchId);
    
    Integer getPhoneCount(Integer batchId,Integer status);
}
