package com.jbb.server.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.server.core.dao.UserDeviceDao;
import com.jbb.server.core.dao.mapper.UserDeviceMapper;
import com.jbb.server.core.domain.UserDevice;

@Repository("UserDeviceDao")
public class UserDeviceDaoImpl implements UserDeviceDao {

    @Autowired
    private UserDeviceMapper userDeviceMapper;

    @Override
    public boolean saveUserDevice(UserDevice userDevice) {
        return userDeviceMapper.saveUserDevice(userDevice) > 0;
    }

    @Override
    public List<UserDevice> selectUserDeviceListByUserId(int userId) {
        return userDeviceMapper.selectUserDeviceListByUserId(userId);
    }

    @Override
    public boolean updateUserDeviceStatus(int userId, String objectId, boolean status) {
         return userDeviceMapper.updateUserDeviceStatus(userId, objectId, status) > 0;
    }

}
