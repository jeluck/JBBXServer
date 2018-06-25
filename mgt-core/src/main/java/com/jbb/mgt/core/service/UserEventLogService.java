package com.jbb.mgt.core.service;

import java.sql.Timestamp;

public interface UserEventLogService {

    // 记录用户操作
    boolean insertEventLog(Integer userId, String sourceId, String cookieId, String eventName, String eventAction,
        String eventLabel, String eventValue, String remoteAddress, Timestamp creationDate);

    // 统计用户操作
    int countEventLogByParams(Integer userId, String sourceId, String cookieId, String eventName, String eventAction,
        String eventLabel, String eventValue, Timestamp start, Timestamp end);
}
