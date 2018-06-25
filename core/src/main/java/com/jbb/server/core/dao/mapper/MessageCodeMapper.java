package com.jbb.server.core.dao.mapper;

import java.sql.Timestamp;

import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.MessageCode;

/**
 * MessageCodeMapper
 * 
 * @author VincentTang
 * @date 2017年12月20日
 */
public interface MessageCodeMapper {
	int saveMessageCode(@Param("phoneNumber") String phoneNumber, 
			@Param("msgCode") String msgCode, 
			@Param("creationDate") Timestamp creationDate, 
			@Param("expireDate") Timestamp expireDate);
	
	int checkMsgCode(@Param("phoneNumber") String phoneNumber, 
	    @Param("msgCode")  String msgCode,
	    @Param("date") Timestamp date);
	
	//Remove later
	 MessageCode selectisnull(@Param("phoneNumber") String phoneNumber,@Param("msgCode") String msgCode);
}

