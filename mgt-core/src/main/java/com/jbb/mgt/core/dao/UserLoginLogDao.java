package com.jbb.mgt.core.dao;


import java.sql.Timestamp;

public interface UserLoginLogDao {

    // 插入User登录日志
    void insertUserLoginLog(int userId, String ipAddress, Timestamp loginDate);
}
