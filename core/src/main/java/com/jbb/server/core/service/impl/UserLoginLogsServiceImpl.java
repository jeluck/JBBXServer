package com.jbb.server.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.server.core.dao.UserLoginLogsDao;
import com.jbb.server.core.domain.UserLoginLog;
import com.jbb.server.core.service.UserLoginLogsService;
@Service("UserLoginLogsService")
public class UserLoginLogsServiceImpl implements UserLoginLogsService {
@Autowired
private UserLoginLogsDao userLoginLogsDao;
	@Override
	public boolean saveUserLoginLogs(UserLoginLog userLoginLogs) {
		return userLoginLogsDao.insertSelective(userLoginLogs)>0;
	}

}
