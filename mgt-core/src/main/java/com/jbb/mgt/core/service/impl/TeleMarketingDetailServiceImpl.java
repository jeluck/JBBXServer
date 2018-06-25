package com.jbb.mgt.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.dao.TeleMarketingDetailDao;
import com.jbb.mgt.core.domain.TeleMarketingDetail;
import com.jbb.mgt.core.service.TeleMarketingDetailService;

/**
 * 批次明细数据service实现类
 * 
 * @author wyq
 * @date 2018/04/29
 */
@Service("TeleMarketingDetailService")
public class TeleMarketingDetailServiceImpl implements TeleMarketingDetailService {

    @Autowired
    private TeleMarketingDetailDao dao;

    @Override
    public List<TeleMarketingDetail> selectRecentTeleMarketingDetail(int accountId) {
        return dao.selectMaxTeleMarketingDetail(accountId);
    }

    @Override
    public List<TeleMarketingDetail> selectTeleMarketingDetails(Integer batchId) {
        return dao.selectTeleMarketingDetails(batchId);
    }

    @Override
    public int insertMobiles(int batchId, List<TeleMarketingDetail> mobiles) {
        return dao.insertMobiles(batchId, mobiles);
    }

    @Override
    public boolean updateMobile(TeleMarketingDetail mobile) {
        return dao.updateMobile(mobile);
    }

    @Override
    public List<TeleMarketingDetail> selectTeleMarketingDetailsNotInit(int batchId, boolean test) {
        return dao.selectTeleMarketingDetailsNotInit(batchId, test);
    }

    @Override
    public List<TeleMarketingDetail> selectTeleMarketingByStatus(int status, int limit,int batchId) {
         return dao.selectTeleMarketingByStatus(status, limit,batchId);
    }

    @Override
    public Integer getPhoneCount(Integer batchId, Integer status) {
         return dao.getPhoneCount(batchId, status);
    }

}
