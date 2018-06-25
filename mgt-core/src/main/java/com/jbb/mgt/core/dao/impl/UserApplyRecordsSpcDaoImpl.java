package com.jbb.mgt.core.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.UserApplyRecordsSpcDao;
import com.jbb.mgt.core.dao.mapper.UserApplyRecordsSpcMapper;
import com.jbb.mgt.core.domain.UserApplyRecordsSpc;

@Repository("UserApplyRecordsSpcDao")
public class UserApplyRecordsSpcDaoImpl implements UserApplyRecordsSpcDao {

    @Autowired
    private UserApplyRecordsSpcMapper mapper;

    @Override
    public void insertUserApplay(Integer userId, Integer accountId) {
        mapper.insertUserApplay(userId, accountId);
    }

    @Override
    public List<UserApplyRecordsSpc> selectAppliesByAccountId(Integer accountId, Timestamp startDate,
        Timestamp endDate) {
        return mapper.selectAppliesByAccountId(accountId, startDate, endDate);
    }

    @Override
    public Integer countApplies(Integer accountId, Timestamp startDate, Timestamp endDate) {
        return mapper.countApplies(accountId, startDate, endDate);
    }

}
