package com.jbb.mgt.core.dao.mapper;

import com.jbb.mgt.core.domain.AccountOpLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账号操作日志mapper类
 * 
 * @author wyq
 * @date 2018-4-23 16:39:57
 *
 */
public interface AccountOpLogMapper {

    void insertOpLog(AccountOpLog accountOpLog);

    /**
     * 查询日志信息
     *
     * @param applyId
     * @return
     */
    List<AccountOpLog> selectAccountOpLogByApplyId(@Param(value = "applyId") Integer applyId);

    Integer getCount(@Param(value = "accountId") Integer accountId, @Param(value = "ops") Integer[] ops, @Param(value = "newtype") Boolean newtype);

}
