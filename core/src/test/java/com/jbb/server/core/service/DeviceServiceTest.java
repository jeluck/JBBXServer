package com.jbb.server.core.service;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Home;
import com.jbb.server.common.util.StringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class DeviceServiceTest {

    @Autowired
    private DeviceService deviceService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testDevice() {
        String seqNumber = "testSeqNumber" + StringUtil.getRandomnum(16);
        String  sourceId="001";
        boolean ret = deviceService.saveDeviceSeqNumber(seqNumber,sourceId);
        assertTrue(ret);
    }
}
