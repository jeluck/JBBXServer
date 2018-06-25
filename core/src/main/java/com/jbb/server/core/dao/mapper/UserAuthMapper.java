package com.jbb.server.core.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.UserAuth;

/**
 * @Type SessionInfoMapper.java
 * @Desc 
 * @author VincentTang
 * @date 2017年10月30日 下午2:50:38
 * @version 
 */
public interface UserAuthMapper {
	int insertUserAuth(UserAuth userAuth);

	void updateUserAuth(UserAuth userAuth);

	UserAuth selectUserAuthBySessionId(@Param("sessionId") String sessionId);
	
	UserAuth selectUserAuthByOpenId(@Param("openId") String openId);

}


/**
 * Revision history
 * -------------------------------------------------------------------------
 * 
 * Date Author Note
 * -------------------------------------------------------------------------
 * 2017年10月30日 VincentTang creat
 */