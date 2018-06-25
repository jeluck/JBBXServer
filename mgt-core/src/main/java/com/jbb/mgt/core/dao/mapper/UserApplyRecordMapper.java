package com.jbb.mgt.core.dao.mapper;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.Statistics;
import com.jbb.mgt.core.domain.UserApplyRecord;

/**
 * UserApplyRecordMapper
 * 
 * @author wyq
 * @date 2018/04/24
 */
public interface UserApplyRecordMapper {

    void insertUserApplyRecord(UserApplyRecord userApplyRecord);

    void updateUserAppayRecord(UserApplyRecord userApplyRecord);

    /**
     * 
     * @param applyId 申请ID
     * @param op 当前审批步骤：assign, init ,final, loan
     * @param accountId 操作人账户ID
     * @param orgId 组织ID
     * @param statuses 状态
     * @param phoneNumberSearch 用户手机搜索
     * @param channelSearch 来源渠道搜索
     * @param usernameSearch 用户名搜索
     * @param assignNameSearch 分配员搜索
     * @param initNameSearch 初审员搜索
     * @param finalNameSearch 复审员搜索
     * @param loandNameSearch 放款员搜索
     * @param idcardSearch 用户身份证搜索
     * @param jbbIdSearch 用户借帮帮ID搜索
     * @return
     */
    List<UserApplyRecord> selectUserApplyRecords(@Param(value = "applyId") Integer applyId,
        @Param(value = "op") String op, @Param(value = "accountId") Integer accountId,
        @Param(value = "orgId") Integer orgId, @Param(value = "statuses") int[] statuses,
        @Param(value = "phoneNumberSearch") String phoneNumberSearch,
        @Param(value = "channelSearch") String channelSearch, @Param(value = "usernameSearch") String usernameSearch,
        @Param(value = "assignNameSearch") String assignNameSearch,
        @Param(value = "initNameSearch") String initNameSearch,
        @Param(value = "finalNameSearch") String finalNameSearch,
        @Param(value = "loanNameSearch") String loanNameSearch, @Param(value = "idcardSearch") String idcardSearch,
        @Param(value = "jbbIdSearch") String jbbIdSearch);

    void updateUserAppayRecordAssignAccIdInBatch(@Param(value = "applyIds") int[] applyIds,
        @Param(value = "assignAccId") int assignAccId, @Param(value = "assignDate") Timestamp assignDate,
        @Param(value = "initAccId") int initAccId);

    List<UserApplyRecord> selectUnassignUserApplyRecords(@Param(value = "limit") Integer limit);

    /**
     * 渠道或帮帮导流今日推送
     * 
     * @param ishidden 是否隐藏渠道 要据orgId灵活的统计导流数据。
     * @param orgId 组织ID
     * @param startDate 统计开始时间， null 无开始时间
     * @param endDate 统计结束时间， null 无结束时间
     * @param isJbbChannel true 借帮帮导流统计，false 自有渠道统计
     * @param includeHidden true 包含隐藏渠道统计, false 不包含隐藏渠道统计
     * @return
     */
    int countUserApplyRecords(@Param(value = "orgId") int orgId, @Param(value = "startDate") Timestamp startDate,
        @Param(value = "endDate") Timestamp endDate, @Param(value = "isJbbChannel") boolean isJbbChannel,
        @Param(value = "includeHidden") boolean includeHidden);

    /**
     * 查询首页统计数据
     * 
     * @param orgId
     * @param startDate
     * @param endDate
     * @return
     */
    Statistics selectUserAppayRecordsCountByApplyId(@Param(value = "orgId") Integer orgId,
        @Param(value = "startDate") Timestamp startDate, @Param(value = "endDate") Timestamp endDate);

    UserApplyRecord selectUserApplyRecordInfoByApplyId(@Param(value = "applyId") Integer applyId);

    /**
     * 查询统计数
     *
     * @param statuses
     * @param startDate
     * @param endDate
     * @return
     */
    int getStatisticsNumber(@Param(value = "statuses") Integer[] statuses,
        @Param(value = "startDate") Timestamp startDate, @Param(value = "endDate") Timestamp endDate);

    Integer getDiversionCount(@Param(value = "orgId") Integer orgId, @Param(value = "type") Integer type);

    // 根据组织统计每日导流量
    int countUserApply(@Param(value = "orgId") Integer orgId, @Param(value = "startDate") Timestamp startDate,
        @Param(value = "endDate") Timestamp endDate);

    List<UserApplyRecord> selectUserApplyRecordsByOrgId(@Param(value = "orgId") Integer orgId,
        @Param(value = "startDate") Timestamp startDate, @Param(value = "endDate") Timestamp endDate);
}
