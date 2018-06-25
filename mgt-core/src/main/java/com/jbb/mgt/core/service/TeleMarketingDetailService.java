package com.jbb.mgt.core.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.TeleMarketingDetail;

/**
 * 批次明细数据Service接口
 * 
 * @author wyq
 * @date 2018/04/29
 */
public interface TeleMarketingDetailService {

    List<TeleMarketingDetail> selectRecentTeleMarketingDetail(int accountId);

    List<TeleMarketingDetail> selectTeleMarketingDetails(Integer batchId);

    int insertMobiles(@Param(value = "batchId") int batchId, List<TeleMarketingDetail> mobiles);

    boolean updateMobile(TeleMarketingDetail mobile);

    /**
     * 查询该批次中在detail表中且不在init表中
     * 
     * @param batchId
     * @return
     */
    List<TeleMarketingDetail> selectTeleMarketingDetailsNotInit(int batchId, boolean test);
    
    List<TeleMarketingDetail> selectTeleMarketingByStatus(int status, int limit, int batchId);
    
    Integer getPhoneCount(Integer batchId,Integer status);
}
