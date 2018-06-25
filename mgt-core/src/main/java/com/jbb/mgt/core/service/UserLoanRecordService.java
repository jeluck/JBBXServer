package com.jbb.mgt.core.service;

import java.util.List;

import com.jbb.mgt.core.domain.UserLoanRecord;
import com.jbb.mgt.core.domain.UserLoanRecordDetail;

public interface UserLoanRecordService {

    List<UserLoanRecord> getUserLoanRecords(Integer loanId, Integer accountId, String op, Integer orgId, int[] statuses,
        int[] iouStatuses, Integer iouDateType, String phoneNumberSearch, String channelSearch, String usernameSearch,
        String idcardSearch, String jbbIdSearch);

    void createUserLoanRecord(UserLoanRecord userLoanRecord);

    void updateUserLoanRecord(UserLoanRecord userLoanRecord);

    void updateUserLoanRecordByIouCode(UserLoanRecord userLoanRecord,int beforeIouStatus);

    UserLoanRecord getUserLoanRecordByLoanId(int loanId);

    void saveUserLoanRecordDetail(UserLoanRecordDetail loanRecordDetail);

    List<UserLoanRecordDetail> getUserLoanRecordDetails(int loanId);

    UserLoanRecord selectUserLoanRecordByIouCode(String iouCode);

}
