package com.jbb.mgt.core.service;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.mgt.core.domain.StatisticsMoney;
import com.jbb.mgt.core.domain.UserApplyRecord;

/**
 * UserApplyRecordService接口
 * 
 * @author wyq
 * @date 2018/04/24
 */
public interface UserApplyRecordService {

    /**
     * 新增申请
     * 
     * @param userId
     * @param accountId
     */
    void createUserApplyRecord(UserApplyRecord userApplyRecord);

    /**
     * 更新
     * 
     * @param userApplyRecord
     */
    void updateUserAppayRecord(UserApplyRecord userApplyRecord);

    List<UserApplyRecord> getUnassignUserApplyRecords(Integer limit);

    /**
     * 筛选用户
     * 
     * @param applyId
     * @param op
     * @param accountId
     * @param orgId
     * @param statuses
     * @param phoneNumberSearch
     * @param channelSearch
     * @param usernameSearch
     * @param assignNameSearch
     * @param initNameSearch
     * @param finalNameSearch
     * @param loandNameSearch
     * @param idcardSearch
     * @param jbbIdSearch
     * @return
     */
    List<UserApplyRecord> getUserApplyRecords(Integer applyId, String op, Integer accountId, Integer orgId,
        int[] statuses, String phoneNumberSearch, String channelSearch, String usernameSearch, String assignNameSearch,
        String initNameSearch, String finalNameSearch, String loandNameSearch, String idcardSearch, String jbbIdSearch);

    UserApplyRecord getUserApplyRecordByApplyId(Integer applyId, String op, Integer accountId, Integer orgId);

    /**
     * 要据orgId灵活的统计导流数据。
     * 
     * @param orgId 组织ID
     * @param startDate 统计开始时间， null 无开始时间
     * @param endDate 统计结束时间， null 无结束时间
     * @return
     */
    int countUserApplyRecords(int orgId, String channelCode, Timestamp startDate, Timestamp endDate);

    /**
     * 查询首页统计数据
     * 
     * @param startDate
     * @param endDate
     * @param orgId
     * @return
     */
    StatisticsMoney selectIndexCountData(Timestamp startDate, Timestamp endDate, Integer orgId);

    void updateUserAppayRecordAssignAccIdInBatch(int[] applyIds, int assignAccId, Timestamp assignDate, int initAccId);

    UserApplyRecord selectUserApplyRecordInfoByApplyId(Integer applyId);

    /**
     * 查询审核统计数
     * 
     * @param statuses
     * @param startDate
     * @param endDate
     * @return
     */
    int getStatisticsNumber(Integer[] statuses, Timestamp startDate, Timestamp endDate);

    Integer getDiversionCount(Integer orgId, Integer type);

    // 根据组织统计每日导流量
    int countUserApply(Integer orgId, Timestamp startDate, Timestamp endDate);

    List<UserApplyRecord> selectUserApplyRecordsByOrgId(Integer orgId, Timestamp startDate, Timestamp endDate);
}
