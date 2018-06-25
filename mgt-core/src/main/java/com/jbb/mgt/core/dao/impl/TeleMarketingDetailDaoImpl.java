package com.jbb.mgt.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.TeleMarketingDetailDao;
import com.jbb.mgt.core.dao.mapper.TeleMarketingDetailMapper;
import com.jbb.mgt.core.domain.TeleMarketingDetail;

/**
 * 批次明细数据Dao接口
 * 
 * @author wyq
 * @date 2018/04/29
 */
@Repository("TeleMarketingDetailDao")
public class TeleMarketingDetailDaoImpl implements TeleMarketingDetailDao {

    @Autowired
    private TeleMarketingDetailMapper mapper;

    @Override
    public List<TeleMarketingDetail> selectMaxTeleMarketingDetail(int accountId) {
        return mapper.selectMaxTeleMarketingDetail(accountId);
    }

    @Override
    public List<TeleMarketingDetail> selectTeleMarketingDetails(Integer betchId) {
        return mapper.selectTeleMarketingDetails(betchId);
    }

    @Override
    public int insertMobiles(int batchId, List<TeleMarketingDetail> mobiles) {
        return mapper.insertMobiles(batchId, mobiles);
    }

    @Override
    public boolean updateMobile(TeleMarketingDetail mobile) {
        return mapper.updateMobile(mobile) > 0;
    }

    @Override
    public List<TeleMarketingDetail> selectTeleMarketingDetailsNotInit(int batchId, boolean test) {
        return mapper.selectTeleMarketingDetailsNotInit(batchId, test);
    }

    @Override
    public List<TeleMarketingDetail> selectTeleMarketingByStatus(int status, int limit,int batchId) {
         return mapper.selectTeleMarketingByStatus(status, limit,batchId);
    }

    @Override
    public Integer getPhoneCount(Integer batchId, Integer status) {
         return mapper.getPhoneCount(batchId,status);
    }

}
