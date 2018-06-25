package com.jbb.mgt.core.dao.impl;

import java.util.List;

import com.jbb.server.common.util.DateUtil;
import com.jbb.server.shared.rs.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.TeleMarketingDao;
import com.jbb.mgt.core.dao.mapper.TeleMarketingDetailMapper;
import com.jbb.mgt.core.dao.mapper.TeleMarketingMapper;
import com.jbb.mgt.core.domain.TeleMarketing;
import com.jbb.mgt.core.domain.TeleMarketingDetail;
import com.jbb.mgt.core.domain.TeleMarketingInit;

/**
 * 电销批次Dao实现类
 * 
 * @author wyq
 * @date 2018/04/29
 */
@Repository("TeleMarketingDao")
public class TeleMarketingDaoImpl implements TeleMarketingDao {

    @Autowired
    private TeleMarketingMapper mapper;

    @Autowired
    private TeleMarketingDetailMapper detailMapper;

    @Override
    public List<TeleMarketing> selectTeleMarketings(int orgId) {
        return mapper.selectTeleMarketings(orgId);
    }

    @Override
    public TeleMarketing selectTeleMarkByBatchId(int batchId,boolean b) {
        return mapper.selectTeleMarkByBatchId(batchId,b);
    }

    @Override
    public TeleMarketing selectMaxTeleMarketings(int accountId) {
        return mapper.selectMaxTeleMarketings(accountId);
    }

    @Override
    public void insertTeleMarketing(TeleMarketing batchInfo) {
        mapper.insertTeleMarketing(batchInfo);
    }

    @Override
    public int insertMobiles(int batchId, List<TeleMarketingDetail> mobiles) {
        return detailMapper.insertMobiles(batchId, mobiles);
    }

    @Override
    public boolean updateMobile(TeleMarketingDetail mobile) {
        return detailMapper.updateMobile(mobile) > 0;
    }

    @Override
    public List<TeleMarketingDetail> selectMobiles(Integer batchId, int orgId) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 查询当前账号需要预审批的明细
     * 
     * @param accountId
     * @return
     */
    @Override
    public List<TeleMarketingInit> selectMobilesByAccountId(Integer accountId) {
        return detailMapper.selectMobilesByAccountId(accountId);
    }

    @Override
    public void updateTeleMarketingInitByIdAndOpCommet(Integer id, String opCommet) {
        detailMapper.updateTeleMarketingInitByIdAndOpCommet(id, opCommet, DateUtil.getCurrentTimeStamp());
    }

    @Override
    public TeleMarketingInit selectTeleMarketingInitById(Integer id) {
        return detailMapper.selectTeleMarketingInitById(id);
    }

    @Override
    public int insertTeleInits(List<TeleMarketingInit> mobileInits) {
        return detailMapper.insertTeleInits(mobileInits);
    }

    @Override
    public List<TeleMarketingInit> selectTeleInits(int accountId, boolean detail, Boolean isMarked) {
        return mapper.selectTeleInits(accountId, detail, isMarked);
    }

    @Override
    public List<TeleMarketing> selectTeleMarkBystatus(int status) {
        return mapper.selectTeleMarkBystatus(status);
    }

    @Override
    public void updateTeleMarketing(TeleMarketing teleMarketing) {
        mapper.updateTeleMarketing(teleMarketing);

    }
}
