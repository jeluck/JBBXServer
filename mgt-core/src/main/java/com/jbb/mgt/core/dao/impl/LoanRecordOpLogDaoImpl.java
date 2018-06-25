package com.jbb.mgt.core.dao.impl;

import com.jbb.mgt.core.dao.LoanRecordOpLogDao;
import com.jbb.mgt.core.dao.mapper.LoanRecordOpLogMapper;
import com.jbb.mgt.core.domain.LoanOpLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("LoanRecordOpLogDao")
public class LoanRecordOpLogDaoImpl implements LoanRecordOpLogDao {

    @Autowired
    LoanRecordOpLogMapper mapper;

    @Override
    public void insertOpLog(LoanOpLog loanOpLog) {
        mapper.insertOpLog(loanOpLog);
    }

    @Override
    public List<LoanOpLog> selectLoanRecordOpLogByLoanId(Integer loanId, Integer[] opTypes) {
        return mapper.selectLoanRecordOpLogByLoanId(loanId, opTypes);
    }

    @Override
    public Integer getmoneyCount(Integer accountId, Integer op, Boolean newtype) {
        return mapper.getmoneyCount(accountId, op, newtype);
    }
}
