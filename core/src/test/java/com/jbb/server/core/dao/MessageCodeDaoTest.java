package com.jbb.server.core.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Home;
import com.jbb.server.common.util.DateUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class MessageCodeDaoTest {
    @Autowired
    private MessageCodeDao messageCodeDao;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testMssageCode() {
        long time = System.currentTimeMillis();
        String phoneNumber = "18575511205";
        String msgCode = "3333";
        Timestamp creationDate = DateUtil.getCurrentTimeStamp();
        Timestamp expire = DateUtil.calTimestamp(time, 5 * 60 * 1000);
        messageCodeDao.saveMessageCode(phoneNumber, msgCode, creationDate, expire);
        boolean ret = messageCodeDao.checkMsgCode(phoneNumber, msgCode, DateUtil.getCurrentTimeStamp());
        assertTrue(ret);
        Timestamp expired = DateUtil.calTimestamp(time, 10 * 60 * 1000);
        ret = messageCodeDao.checkMsgCode(phoneNumber, msgCode, expired);
        assertFalse(ret);
    }

}
