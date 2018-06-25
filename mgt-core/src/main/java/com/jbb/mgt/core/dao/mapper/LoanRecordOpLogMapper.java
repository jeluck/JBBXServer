package com.jbb.mgt.core.dao.mapper;

import com.jbb.mgt.core.domain.LoanOpLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoanRecordOpLogMapper {

    // 新增加
    void insertOpLog(LoanOpLog loanOpLog);

    /**
     * 查询日志信息
     *
     * @param loanId
     * @return
     */
    List<LoanOpLog> selectLoanRecordOpLogByLoanId(@Param(value = "loanId") Integer loanId,@Param(value = "opTypes") Integer[] opTypes);

    Integer getmoneyCount(@Param(value = "accountId") Integer accountId, @Param(value = "op") Integer op, @Param(value = "newtype") Boolean newtype);

}
