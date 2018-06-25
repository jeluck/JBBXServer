package com.jbb.mgt.core.dao;

import com.jbb.mgt.core.domain.AccountOpLog;

import java.util.List;

public interface AccountOpLogDao {
    // 新增加
    void insertOpLog(AccountOpLog accountOpLog);

    /**
     * 查询日志信息
     * 
     * @param applyId
     * @return
     */
    List<AccountOpLog> selectAccountOpLogByApplyId(Integer applyId);

    Integer getCount(Integer accountId, Integer[] ops, Boolean newtype);

}
