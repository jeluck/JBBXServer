package com.jbb.mgt.core.dao.mapper;

import java.sql.Timestamp;

import org.apache.ibatis.annotations.Param;

public interface UserLoginLogMapper {
    void insertUserLoginLog(@Param(value = "userId") int userId, @Param(value = "ipAddress") String ipAddress,
        @Param(value = "loginDate") Timestamp loginDate);
}
