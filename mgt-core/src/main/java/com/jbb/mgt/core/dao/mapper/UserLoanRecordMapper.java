package com.jbb.mgt.core.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.UserLoanRecord;
import com.jbb.mgt.core.domain.UserLoanRecordDetail;

public interface UserLoanRecordMapper {

    // add by vincent
    List<UserLoanRecord> selectUserLoanRecords(@Param(value = "loanId") Integer loanId,
        @Param(value = "accountId") Integer accountId,  @Param(value = "op") String op,
        @Param(value = "orgId") Integer orgId, @Param(value = "statuses") int[] statuses,
        @Param(value = "iouStatuses") int[] iouStatuses,  @Param(value = "iouDateType") Integer iouDateType, 
        @Param(value = "phoneNumberSearch") String phoneNumberSearch,
        @Param(value = "channelSearch") String channelSearch, @Param(value = "usernameSearch") String usernameSearch,
        @Param(value = "idcardSearch") String idcardSearch, @Param(value = "jbbIdSearch") String jbbIdSearch);

    void insertUserLoanRecord(UserLoanRecord userLoanRecord);

    void updateUserLoanRecord(UserLoanRecord userLoanRecord);

    void updateUserLoanRecordByIouCode(UserLoanRecord userLoanRecord);
    // end add

    // 更新状态
    void updateUserLoanRecordStatus(@Param(value = "loanId") String loanId, @Param(value = "status") int status);

    // 根据loanId删除
    boolean deleteUserLoanRecord(@Param(value = "loanId") String loanId);

    // 根据loanId查询
    UserLoanRecord selectUserLoanRecordById(@Param(value = "loanId") String loanId);

    UserLoanRecord selectUserLoanRecordByIouCode(@Param(value = "iouCode")String iouCode);

    // 根据AccId查询
    List<UserLoanRecord> selectUserLoanRecordByorgId(@Param(value = "accountId") int accountId);

    // 根据id修改信息
    void updateUserLoanRecordById(UserLoanRecord userLoanRecord);

    // 插入明细
    void insertUserLoanRecordDetail(UserLoanRecordDetail userLoanRecordDetails);

    // 更新明细
    void updateUserLoanRecordDetail(UserLoanRecordDetail userLoanRecordDetails);

    // 选择账单明细
    List<UserLoanRecordDetail> selectUserLoanRecordDetails(@Param(value = "loanId") String loanId,
        @Param("detail") boolean detail);

    UserLoanRecordDetail selectUserLoanRecordDetail(@Param(value = "accountId") int accountId,
        @Param("detail") boolean detail);

    List<UserLoanRecord> selectUserLoanRecordByCondition(@Param(value = "accountId") int accountId,
        @Param(value = "status") int status, @Param(value = "phoneNumber") String phoneNumber,
        @Param(value = "userName") String userName, @Param(value = "idCard") String idCard,
        @Param(value = "channelCode") String channelCode);

    List<UserLoanRecord> getUserLoanRecordByStatus(@Param(value = "accountId") int accountId,
        @Param(value = "status") int status);
}
