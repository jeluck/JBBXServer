package com.jbb.mgt.core.dao;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.mgt.core.domain.UserApplyRecordsSpc;

public interface UserApplyRecordsSpcDao {
    void insertUserApplay(Integer userId, Integer accountId);

    List<UserApplyRecordsSpc> selectAppliesByAccountId(Integer accountId,Timestamp startDate, Timestamp endDate);

    Integer countApplies(Integer accountId,Timestamp startDate, Timestamp endDate);

}
