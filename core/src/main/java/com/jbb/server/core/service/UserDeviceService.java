package com.jbb.server.core.service;

import java.util.List;

import com.jbb.server.core.domain.UserDevice;

public interface UserDeviceService {

    boolean saveUserDevice(UserDevice userDevice);

    /**
     * @param userId 根据userId 获取用户的设备信息，存在多平台的情况 ios/android 等
     * @return
     */
    List<UserDevice> getUserDeviceListByUserId(int userId);
    
    void logoutUserDevice(int userId, String objectId);

}
