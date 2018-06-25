package com.jbb.mgt.core.dao.mapper;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.UserApplyRecordsSpc;

public interface UserApplyRecordsSpcMapper {

    void insertUserApplay(@Param(value = "userId") Integer userId, @Param(value = "accountId") Integer accountId);

    List<UserApplyRecordsSpc> selectAppliesByAccountId(@Param(value = "accountId") Integer accountId,
        @Param(value = "startDate") Timestamp startDate, @Param(value = "endDate") Timestamp endDate);

    Integer countApplies(@Param(value = "accountId") Integer accountId, @Param(value = "startDate") Timestamp startDate,
        @Param(value = "endDate") Timestamp endDate);

}