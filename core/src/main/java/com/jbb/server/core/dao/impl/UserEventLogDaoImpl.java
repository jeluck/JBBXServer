package com.jbb.server.core.dao.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.server.core.dao.UserEventLogDao;
import com.jbb.server.core.dao.mapper.UserEventLogMapper;

@Repository("UserEventLogDao")
public class UserEventLogDaoImpl implements UserEventLogDao {

    @Autowired
    private UserEventLogMapper mapper;

    @Override
    public boolean insertEventLog(Integer userId, String sourceId, String eventName, String eventAction,
        String eventLabel, String eventValue, String remoteAddress, Timestamp creationDate) {
        return mapper.insertEventLog(userId, sourceId, eventName, eventAction, eventLabel, eventValue, remoteAddress,
            creationDate) > 0;
    }

    @Override
    public int countEventLogByParams(Integer userId, String sourceId, String eventName, String eventAction,
        String eventLabel, String eventValue, Timestamp start, Timestamp end) {
        return mapper.countEventLogByParams(userId, sourceId, eventName, eventAction, eventLabel, eventValue, start,
            end);
    }

}
