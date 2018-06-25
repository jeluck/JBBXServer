package com.jbb.server.core.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.domain.UserDevice;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class UserDeviceDaoTest {

    @Autowired
    private UserDeviceDao userDeviceDao;

    private static int testUserId = 1000000;

    @Test
    public void testUserDevice() {

        UserDevice userDevice = new UserDevice();
        userDevice.setCreationDate(DateUtil.getCurrentTimeStamp());
        userDevice.setCreationDate(DateUtil.getCurrentTimeStamp());
        userDevice.setDeviceToken("123");
        userDevice.setDeviceType("android");
        userDevice.setInstallationId("xxxxx");
        userDevice.setUserId(testUserId);
        userDeviceDao.saveUserDevice(userDevice);

        userDevice = new UserDevice();
        userDevice.setDeviceToken("231213");
        userDevice.setDeviceType("android");
        userDevice.setInstallationId("");
        userDevice.setObjectId("312123");
        userDevice.setUserId(testUserId);
        userDeviceDao.saveUserDevice(userDevice);

        List<UserDevice> list = userDeviceDao.selectUserDeviceListByUserId(testUserId);
        UserDevice userDeviceU = null;
        for (UserDevice ud : list) {
            if (ud.getObjectId().equals(userDevice.getObjectId())) {
                userDeviceU = ud;
                break;
            }
        }

        assertEquals(userDeviceU.getInstallationId(), userDevice.getInstallationId());
        assertEquals(userDeviceU.getDeviceType(), userDevice.getDeviceType());
    }

}
