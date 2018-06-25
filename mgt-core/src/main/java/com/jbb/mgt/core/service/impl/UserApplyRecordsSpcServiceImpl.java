package com.jbb.mgt.core.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.dao.UserApplyRecordsSpcDao;
import com.jbb.mgt.core.domain.UserApplyRecordsSpc;
import com.jbb.mgt.core.service.UserApplyRecordsSpcService;

@Service("UserApplyRecordsSpcService")
public class UserApplyRecordsSpcServiceImpl implements UserApplyRecordsSpcService {
    @Autowired
    private UserApplyRecordsSpcDao userApplyRecordsSpcDao;

    @Override
    public void insertUserApplay(Integer userId, Integer accountId) {
        userApplyRecordsSpcDao.insertUserApplay(userId, accountId);
    }

    @Override
    public List<UserApplyRecordsSpc> selectAppliesByAccountId(Integer accountId, Timestamp startDate,
        Timestamp endDate) {
        return userApplyRecordsSpcDao.selectAppliesByAccountId(accountId, startDate, endDate);
    }

    @Override
    public Integer countApplies(Integer accountId, Timestamp startDate, Timestamp endDate) {
        return userApplyRecordsSpcDao.countApplies(accountId, startDate, endDate);
    }

}
