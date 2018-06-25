package com.jbb.server.core.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.server.core.dao.ClubUsersDao;
import com.jbb.server.core.dao.mapper.ClubUserMapper;
import com.jbb.server.core.domain.ClubUser;

@Repository("ClubUsersDao")
public class ClubUserDaoImpl implements ClubUsersDao {

	@Autowired
	private ClubUserMapper mapper;
	
	@Override
	public int insertUser(ClubUser user) {
		return mapper.insertClubUser(user);
	}
	




}
