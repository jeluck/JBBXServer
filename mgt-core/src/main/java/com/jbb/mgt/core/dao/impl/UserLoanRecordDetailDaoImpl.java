package com.jbb.mgt.core.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.UserLoanRecordDetailDao;
import com.jbb.mgt.core.dao.mapper.UserLoanRecordDetailMapper;
import com.jbb.mgt.core.domain.Money;
import com.jbb.mgt.core.domain.UserLoanRecordDetail;

import java.sql.Timestamp;
import java.util.List;

@Repository("UserLoanRecordDetailDao")
public class UserLoanRecordDetailDaoImpl implements UserLoanRecordDetailDao {

    @Autowired
    private UserLoanRecordDetailMapper mapper;

    @Override
    public Money selectCountMoneyByOrgId(Integer orgId, Timestamp startDate, Timestamp endDate) {
        return mapper.selectCountMoneyByOrgId(orgId, startDate, endDate);
    }

    @Override
    public int selectDueMoney(Integer orgId) {
        return mapper.selectDueMoney(orgId);
    }

    @Override
    public int selectBorrowingMoney(Integer orgId, Timestamp startDate, Timestamp endDate) {
        return mapper.selectBorrowingMoney(orgId,startDate,endDate);
    }

    @Override
    public void insertUserLoanRecordDetail(UserLoanRecordDetail loanRecordDetail) {
       mapper.insertUserLoanRecordDetail(loanRecordDetail);
         
    }

    @Override
    public List<UserLoanRecordDetail> selectUserLoanRecordDetails(int loanId) {
         return mapper.selectUserLoanRecordDetails(loanId);
    }

}
