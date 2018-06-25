package com.jbb.mgt.core.dao;

import java.util.List;

import com.jbb.mgt.core.domain.UserLoanRecord;
import com.jbb.mgt.core.domain.UserLoanRecordDetail;

public interface UserLoanRecordDao {

    List<UserLoanRecord> selectUserLoanRecords(Integer loanId, Integer accountId, String op, Integer orgId,
        int[] statuses, int[] iouStatuses, Integer iouDateType, String phoneNumberSearch, String channelSearch, String usernameSearch,
        String idcardSearch, String jbbIdSearch);

    void insertUserLoanRecord(UserLoanRecord userLoanRecord);

    void updateUserLoanRecord(UserLoanRecord userLoanRecord);

    void updateUserLoanRecordByIouCode(UserLoanRecord userLoanRecord);


    UserLoanRecord selectUserLoanRecordByIouCode(String iouCode);

    UserLoanRecord selectUserLoanRecordByLoanId(int loanId);
   

}
