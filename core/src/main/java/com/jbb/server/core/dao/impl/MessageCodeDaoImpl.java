 package com.jbb.server.core.dao.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.server.core.dao.MessageCodeDao;
import com.jbb.server.core.dao.mapper.MessageCodeMapper;
import com.jbb.server.core.domain.MessageCode;

@Repository("MessageCodeDao")
public class MessageCodeDaoImpl implements MessageCodeDao{
	
	@Autowired
	private MessageCodeMapper mapper;

	@Override
	public int saveMessageCode(String phoneNumber, String msgCode, Timestamp creationDate, Timestamp expireDate) {
		return mapper.saveMessageCode(phoneNumber, msgCode, creationDate, expireDate);
		 
	}
	
    @Override
    public boolean checkMsgCode(String phoneNumber, String msgCode, Timestamp date) {
         return mapper.checkMsgCode(phoneNumber, msgCode, date) > 0;
    }
    
    @Override
    public MessageCode selectisnull(String phoneNumber, String msgCode) {
        return mapper.selectisnull(phoneNumber, msgCode);
    }


}
