package com.jbb.mgt.core.dao.impl;

import com.jbb.mgt.core.dao.AccountOpLogDao;
import com.jbb.mgt.core.dao.mapper.AccountOpLogMapper;
import com.jbb.mgt.core.domain.AccountOpLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 账号操作dao实现类
 * 
 * @author wyq
 * @date 2018-4-23 16:39:11
 *
 */
@Repository("AccountOpLogDao")
public class AccountOpLogDaoImpl implements AccountOpLogDao {

    @Autowired
    private AccountOpLogMapper mapper;

    @Override
    public void insertOpLog(AccountOpLog accountOpLog) {
        mapper.insertOpLog(accountOpLog);

    }

    @Override
    public List<AccountOpLog> selectAccountOpLogByApplyId(Integer applyId) {
        return mapper.selectAccountOpLogByApplyId(applyId);
    }

    @Override
    public Integer getCount(Integer accountId, Integer[] ops, Boolean newtype ) {
         return mapper.getCount(accountId,ops,newtype);
    }

}
