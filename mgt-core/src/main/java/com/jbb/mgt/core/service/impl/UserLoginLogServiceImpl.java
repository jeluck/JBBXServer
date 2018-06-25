package com.jbb.mgt.core.service.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.dao.UserLoginLogDao;
import com.jbb.mgt.core.service.UserLoginLogService;

@Service("UserLoginLogService")
public class UserLoginLogServiceImpl implements UserLoginLogService{
	
	@Autowired
	UserLoginLogDao userLoginLogDao;
	@Override
	public void insertUserLoginLog(int userId, String ipAddress, Timestamp loginDate) {
		userLoginLogDao.insertUserLoginLog(userId, ipAddress, loginDate);
		
	}

}
