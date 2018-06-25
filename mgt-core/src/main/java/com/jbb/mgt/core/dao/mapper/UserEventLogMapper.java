 package com.jbb.mgt.core.dao.mapper;

import java.sql.Timestamp;

import org.apache.ibatis.annotations.Param;

public interface UserEventLogMapper {
    
     // 记录用户操作
     int insertEventLog(
         @Param(value = "userId") Integer userId, 
         @Param(value = "sourceId") String sourceId, 
         @Param(value = "cookieId") String cookieId, 
         @Param(value = "eventName") String eventName, 
         @Param(value = "eventAction") String eventAction, 
         @Param(value = "eventLabel") String eventLabel,
         @Param(value = "eventValue") String eventValue, 
         @Param(value = "remoteAddress") String remoteAddress, 
         @Param(value = "creationDate") Timestamp creationDate);
    // 统计用户操作
    int countEventLogByParams(@Param(value = "userId") Integer userId, 
        @Param(value = "sourceId") String sourceId, 
        @Param(value = "cookieId") String cookieId,
        @Param(value = "eventName") String eventName, 
        @Param(value = "eventAction") String eventAction, 
        @Param(value = "eventLabel") String eventLabel,
        @Param(value = "eventValue") String eventValue, 
        @Param(value = "start") Timestamp start,
        @Param(value = "end") Timestamp end
       );

}
