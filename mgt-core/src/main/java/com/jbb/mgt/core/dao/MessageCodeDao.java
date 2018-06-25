package com.jbb.mgt.core.dao;

import java.sql.Timestamp;

public interface MessageCodeDao {

    int saveMessageCode(String phoneNumber, String channelCode, String msgCode, Timestamp creationDate,
        Timestamp expireDate);

    boolean checkMsgCode(String phoneNumber, String channelCode, String msgCode, Timestamp date);

}
