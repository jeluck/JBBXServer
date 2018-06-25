package com.jbb.server.core.dao;

import java.sql.Timestamp;

public interface UserEventLogDao {
    boolean insertEventLog(Integer userId, String sourceId, String eventName, String eventAction, String eventLabel,
        String eventValue, String remoteAddress,  Timestamp creationDate);
    

    int countEventLogByParams(Integer userId, String sourceId, String eventName, String eventAction, String eventLabel,
        String eventValue, Timestamp start, Timestamp end);

}
