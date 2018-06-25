package com.jbb.server.core.dao;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Home;
import com.jbb.server.core.domain.Region;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class RegionDaoTest {
    @Autowired
    private RegionCodeDao regionCodeDao;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void test() {
        // exist
        String code = "430426";
        Region region = regionCodeDao.getRegionByCode(code);
        assertNotNull(region);
        System.out.println(region);
        // not exist
        code = "111";
        region = regionCodeDao.getRegionByCode(code);
        assertNull(region);

        // not exist
        code = "111111";
        region = regionCodeDao.getRegionByCode(code);
        assertNull(region);
    }
}