package com.jbb.mgt.core.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.TeleMarketing;
import com.jbb.mgt.core.domain.TeleMarketingDetail;
import com.jbb.mgt.core.domain.TeleMarketingInit;

/**
 * 电销批次Dao接口
 * 
 * @author wyq
 * @date 2018/04/29
 */
public interface TeleMarketingDao {

    /**
     * 获取批列表
     * 
     * @param orgId
     * @return
     */
    List<TeleMarketing> selectTeleMarketings(int orgId);

    /**
     * 获取批数据记录
     * 
     * @param batchId
     * @return
     */
    TeleMarketing selectTeleMarkByBatchId(int batchId,boolean b);

    /**
     * 获取当前组织批次最大的批次数据
     *
     * @param accountId
     * @return
     */
    TeleMarketing selectMaxTeleMarketings(int accountId);

    /**
     * 插入批数据记录
     * 
     * @param batchInfo
     */
    void insertTeleMarketing(TeleMarketing batchInfo);

    /**
     * 批量插入电话数据明细
     * 
     * @param batchId
     * @param mobiles
     * @return
     */
    int insertMobiles(@Param(value = "batchId") int batchId, List<TeleMarketingDetail> mobiles);

    /**
     * 更新电话明细。进行空号检测等一系列逻辑后，更新电话明细
     * 
     * @param batchId
     * @param mobiles
     * @return
     */
    boolean updateMobile(TeleMarketingDetail mobile);

    /**
     * 按组织和批次数查询批次明细。若batchId为空，获取最近一批的明细。
     * 
     * @param batchId
     * @param orgId
     * @return
     */
    List<TeleMarketingDetail> selectMobiles(Integer batchId, int orgId);

    /**
     * 批量分配或者单个分配
     * 
     * @param mobileInits
     * @return sql 执行成功的数量
     */
    int insertTeleInits(List<TeleMarketingInit> mobileInits);

    /**
     * 初审用户获取分配给自己的电话号码.
     * 
     * @param accountId
     * @param isMarked 是否标记过，op_commet_flag 进行过滤返回。
     * @param detail 是否返回明细。如果为ture, 则返加mobilDetail和batchInfo。
     * @return
     */
    List<TeleMarketingInit> selectTeleInits(int accountId, boolean detail, Boolean isMarked);

    /**
     * 查询当前账号需要预审批的
     * 
     * @param accountId
     * @return
     */
    List<TeleMarketingInit> selectMobilesByAccountId(Integer accountId);

    /**
     * 标记电销号码
     * 
     * @param id
     * @param opCommet
     */
    void updateTeleMarketingInitByIdAndOpCommet(Integer id, String opCommet);

    TeleMarketingInit selectTeleMarketingInitById(Integer id);

    List<TeleMarketing> selectTeleMarkBystatus(int status);

    void updateTeleMarketing(TeleMarketing teleMarketing);
}
