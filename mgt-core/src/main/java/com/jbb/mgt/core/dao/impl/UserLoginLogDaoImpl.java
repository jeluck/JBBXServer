package com.jbb.mgt.core.dao.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.UserLoginLogDao;
import com.jbb.mgt.core.dao.mapper.UserLoginLogMapper;

@Repository("UserLoginLogDao")
public class UserLoginLogDaoImpl implements UserLoginLogDao{

	
	@Autowired
	UserLoginLogMapper userLoginLogMapper;
	
	@Override
	public void insertUserLoginLog(int userId, String ipAddress, Timestamp loginDate) {
		userLoginLogMapper.insertUserLoginLog(userId, ipAddress, loginDate);
	}

}
