 package com.jbb.server.core.dao.mapper;

import java.sql.Timestamp;

import org.apache.ibatis.annotations.Param;

public interface DeviceMapper {
     
     int insertDevice(@Param("equipmentNumber") String equipmentNumber, 
         @Param("creationDate") Timestamp creationDate);

     int checkDevice(@Param("equipmentNumber") String equipmentNumber);

}
