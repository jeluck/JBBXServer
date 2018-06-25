package com.jbb.server.core.service.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.dao.UserEventLogDao;
import com.jbb.server.core.service.UserEventLogService;

@Service("userEventLogService")
public class UserEventLogServiceImpl implements UserEventLogService {

    @Autowired
    private UserEventLogDao userEventLogDao;

    @Override
    public boolean insertEventLog(Integer userId, String sourceId, String eventName, String eventAction,
        String eventLabel, String eventValue, String remoteAddress) {
        Timestamp creationDate = DateUtil.getCurrentTimeStamp();
        return userEventLogDao.insertEventLog(userId, sourceId, eventName, eventAction, eventLabel, eventValue,
            remoteAddress, creationDate);
    }
    
    @Override
    public int countEventLogByParams(Integer userId, String sourceId, String eventName, String eventAction,
        String eventLabel, String eventValue, Timestamp start, Timestamp end) {
        return userEventLogDao.countEventLogByParams(userId, sourceId, eventName, eventAction, eventLabel, eventValue, start, end);
    }

}
