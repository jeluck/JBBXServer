package com.jbb.server.core.dao;

import java.sql.Timestamp;

import com.jbb.server.core.domain.MessageCode;

public interface MessageCodeDao {
	int saveMessageCode(String phoneNumber, 
			String msgCode, 
			Timestamp creationDate, 
			Timestamp expireDate);
	boolean checkMsgCode(String phoneNumber, String msgCode, Timestamp date);
	
	MessageCode selectisnull(String phoneNumber,String msgCode);
}
 
