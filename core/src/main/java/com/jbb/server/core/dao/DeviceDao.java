package com.jbb.server.core.dao;

import java.sql.Timestamp;

public interface DeviceDao {

    boolean insertDevice(String equipmentNumber, Timestamp creationDate);

    boolean checkDeviceExist(String equipmentNumber);

}
