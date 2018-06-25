package com.jbb.mgt.core.service;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.AreaZones;
import com.jbb.server.common.Home;
import com.jbb.server.common.util.IDCardUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class AreaZonesServiceTest {

    @Autowired
    private AreaZonesService areaZonesService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void getAreaZones() {
        String zone = "431021199503080027";
        if (IDCardUtil.validate(zone)) {
            AreaZones areazone = areaZonesService.selectAreazonesByZone(zone.substring(0,6));
            assertTrue(areazone!=null);
        }
    }
}
