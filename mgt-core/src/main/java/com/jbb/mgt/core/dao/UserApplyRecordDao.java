package com.jbb.mgt.core.dao;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.mgt.core.domain.Statistics;
import com.jbb.mgt.core.domain.UserApplyRecord;

public interface UserApplyRecordDao {

    void insertUserApplyRecord(UserApplyRecord userApplyRecord);

    void updateUserAppayRecord(UserApplyRecord userApplyRecord);

    UserApplyRecord selectUserApplyRecordByApplyId(Integer applyId, String op, Integer accountId, Integer orgId);

    List<UserApplyRecord> selectUserApplyRecords(Integer applyId, String op, Integer accountId, Integer orgId,
        int[] statuses, String phoneNumberSearch, String channelSearch, String usernameSearch, String assignNameSearch,
        String initNameSearch, String finalNameSearch, String loandNameSearch, String idcardSearch, String jbbIdSearch);

    void updateUserAppayRecordAssignAccIdInBatch(int[] applyIds, int assignAccId, Timestamp assignDate, int initAccId);

    List<UserApplyRecord> selectUnassignUserApplyRecords(Integer limit);

    /**
     * 要据orgId灵活的统计导流数据。
     * 
     * @param orgId 组织ID
     * @param startDate 统计开始时间， null 无开始时间
     * @param endDate 统计结束时间， null 无结束时间
     * @param isJbbChannel true 借帮帮导流统计，false 自有渠道统计
     * @param includeHidden true 包含隐藏渠道统计, false 不包含隐藏渠道统计
     * @return
     */
    int countUserApplyRecords(int orgId, Timestamp startDate, Timestamp endDate, boolean isJbbChannel,
        boolean includeHidden);

    /**
     * 查询首页统计数据
     * 
     * @param orgId
     * @param startDate
     * @param endDate
     * @return
     */
    Statistics selectUserAppayRecordsCountByApplyId(Integer orgId, Timestamp startDate, Timestamp endDate);

    UserApplyRecord selectUserApplyRecordInfoByApplyId(Integer applyId);

    /**
     * 查询统计数
     *
     * @param statuses
     * @param startDate
     * @param endDate
     * @return
     */
    int getStatisticsNumber(Integer[] statuses, Timestamp startDate, Timestamp endDate);

    Integer getDiversionCount(Integer orgId, Integer type);

    // 根据组织统计每日导流量
    int countUserApply(Integer orgId,Timestamp startDate, Timestamp endDate);

    List<UserApplyRecord> selectUserApplyRecordsByOrgId(Integer orgId, Timestamp startDate, Timestamp endDate);
}
