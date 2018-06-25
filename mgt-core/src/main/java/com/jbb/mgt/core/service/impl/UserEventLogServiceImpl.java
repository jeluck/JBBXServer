package com.jbb.mgt.core.service.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.dao.UserEventLogDao;
import com.jbb.mgt.core.service.UserEventLogService;

@Service("userEventLogService")
public class UserEventLogServiceImpl implements UserEventLogService {

    @Autowired
    private UserEventLogDao userEventLogDao;

    // 记录用户操作
    @Override
    public boolean insertEventLog(Integer userId, String sourceId, String cookieId, String eventName,
        String eventAction, String eventLabel, String eventValue, String remoteAddress, Timestamp creationDate) {
        return userEventLogDao.insertEventLog(userId, sourceId, cookieId, eventName, eventAction, eventLabel,
            eventValue, remoteAddress, creationDate);
    }

    // 统计用户操作
    @Override
    public int countEventLogByParams(Integer userId, String sourceId, String cookieId, String eventName,
        String eventAction, String eventLabel, String eventValue, Timestamp start, Timestamp end) {
        return userEventLogDao.countEventLogByParams(userId, sourceId, cookieId, eventName, eventAction, eventLabel,
            eventValue, start, end);
    }

}
