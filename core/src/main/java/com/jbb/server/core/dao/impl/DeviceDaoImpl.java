package com.jbb.server.core.dao.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.server.core.dao.DeviceDao;
import com.jbb.server.core.dao.mapper.DeviceMapper;

@Repository("DeviceDao")
public class DeviceDaoImpl implements DeviceDao {

    @Autowired
    private DeviceMapper mapper;

    @Override
    public boolean insertDevice(String equipmentNumber, Timestamp creationDate) {
        return mapper.insertDevice(equipmentNumber, creationDate) > 0;
    }

    @Override
    public boolean checkDeviceExist(String equipmentNumber) {
        return mapper.checkDevice(equipmentNumber) > 0;
    }

}
