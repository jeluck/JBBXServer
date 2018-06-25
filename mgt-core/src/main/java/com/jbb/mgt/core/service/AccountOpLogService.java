package com.jbb.mgt.core.service;

import com.jbb.mgt.core.domain.AccountOpLog;

import java.util.List;

/**
 * 账号操作日志service类
 * 
 * @author wyq
 * @date 2018-4-23 15:39:59
 *
 */
public interface AccountOpLogService {

    /**
     * 新增渠道日志
     * 
     */
    void createOpLog(AccountOpLog accountOpLog);

    /**
     * 查询日志信息
     *
     * @param applyId
     * @return
     */
    List<AccountOpLog> selectAccountOpLogByApplyId(Integer applyId);

    Integer getCount(Integer accountId, Integer[] ops, Boolean newtype);
}
