package com.jbb.server.core.service;

import java.sql.Timestamp;

public interface UserEventLogService {
    boolean insertEventLog(Integer userId, String sourceId, String eventName, String eventAction, String eventLabel,
        String eventValue, String remoteAddress);

    int countEventLogByParams(Integer userId, String sourceId, String eventName, String eventAction, String eventLabel,
        String eventValue, Timestamp start, Timestamp end);
}
