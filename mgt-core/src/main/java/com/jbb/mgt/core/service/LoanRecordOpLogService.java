package com.jbb.mgt.core.service;

import com.jbb.mgt.core.domain.LoanOpLog;

import java.util.List;

public interface LoanRecordOpLogService {

    // 新增加
    void insertOpLog(LoanOpLog loanOpLog);

    /**
     * 查询日志信息
     *
     * @param loanId
     * @return
     */
    List<LoanOpLog> selectLoanRecordOpLogByLoanId(Integer loanId,Integer[] opTypes);

    Integer getmoneyCount(Integer accountId, Integer op, Boolean newtype);
}
