package com.jbb.mgt.core.service;

import static org.junit.Assert.assertTrue;

import java.util.Date;

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
public class UserEventLogServiceTest {

    @Autowired
    private UserEventLogService userEventLogService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testUserEventLogs() {

        boolean result = userEventLogService.insertEventLog(1, "001", "test", "test", "test", "test", "test",
            "ip address", DateUtil.getCurrentTimeStamp());
        assertTrue(result);
        int count = userEventLogService.countEventLogByParams(1, null, null, null, null, null, null, null, null);
        assertTrue(count >= 1);
        
    }
}
