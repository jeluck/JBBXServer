package com.jbb.server.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.server.core.dao.AccountDao;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.service.UserIdsService;

@Service("UserIdsService")
public class UserIdsServiceImpl implements UserIdsService{

	@Autowired
	private AccountDao accountDao;
	
	@Override
	public List<User> getUserHeadAndNickName(List<Integer> userIds) {
	    return  accountDao.getUsers(userIds, null, false);
	}

}
