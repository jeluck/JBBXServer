package com.jbb.server.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.server.core.dao.UserDeviceDao;
import com.jbb.server.core.domain.UserDevice;
import com.jbb.server.core.service.UserDeviceService;

@Service("UserDeviceService")
public class UserDeviceServiceImpl implements UserDeviceService {

    @Autowired
    private UserDeviceDao userDeviceDao;

    @Override
    public boolean saveUserDevice(UserDevice userDevice) {
        return userDeviceDao.saveUserDevice(userDevice);
    }

    @Override
    public List<UserDevice> getUserDeviceListByUserId(int userId) {
        return userDeviceDao.selectUserDeviceListByUserId(userId);
    }

    @Override
    public void logoutUserDevice(int userId, String objectId) {
        userDeviceDao.updateUserDeviceStatus(userId, objectId, true);
    }

}
