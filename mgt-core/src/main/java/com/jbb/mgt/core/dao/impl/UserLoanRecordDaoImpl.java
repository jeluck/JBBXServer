package com.jbb.mgt.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.UserLoanRecordDao;
import com.jbb.mgt.core.dao.mapper.UserLoanRecordMapper;
import com.jbb.mgt.core.domain.UserLoanRecord;
import com.jbb.server.common.util.CommonUtil;

@Repository("UserLoanRecordDao")
public class UserLoanRecordDaoImpl implements UserLoanRecordDao {

    @Autowired
    private UserLoanRecordMapper mapper;

   
    // ADDED BY VINCENT
   
    @Override
    public void insertUserLoanRecord(UserLoanRecord userLoanRecord) {
        mapper.insertUserLoanRecord(userLoanRecord);

    }

    @Override
    public void updateUserLoanRecord(UserLoanRecord userLoanRecord) {
        mapper.updateUserLoanRecord(userLoanRecord);

    }

    @Override
    public void updateUserLoanRecordByIouCode(UserLoanRecord userLoanRecord) {
        mapper.updateUserLoanRecordByIouCode(userLoanRecord);
    }

    @Override
    public UserLoanRecord selectUserLoanRecordByIouCode(String iouCode) {
        return mapper.selectUserLoanRecordByIouCode(iouCode);
    }


    @Override
    public List<UserLoanRecord> selectUserLoanRecords(Integer loanId, Integer accountId, String op, Integer orgId,
        int[] statuses, int[] iouStatuses, Integer iouDateType, String phoneNumberSearch, String channelSearch, String usernameSearch,
        String idcardSearch, String jbbIdSearch) {
       
         return mapper.selectUserLoanRecords(loanId, accountId, op, orgId, statuses, iouStatuses, iouDateType, phoneNumberSearch, channelSearch, usernameSearch, idcardSearch, jbbIdSearch);
    }

   
    @Override
    public UserLoanRecord selectUserLoanRecordByLoanId(int loanId) {
        List<UserLoanRecord> list = mapper.selectUserLoanRecords(loanId,null, null, null, null, null, null, null, null,
            null, null, null);
        if (CommonUtil.isNullOrEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }

}
