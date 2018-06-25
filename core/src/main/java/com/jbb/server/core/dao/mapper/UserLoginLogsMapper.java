package com.jbb.server.core.dao.mapper;

import com.jbb.server.core.domain.UserLoginLog;

public interface UserLoginLogsMapper {
    int insert(UserLoginLog record);

    int insertSelective(UserLoginLog record);
}