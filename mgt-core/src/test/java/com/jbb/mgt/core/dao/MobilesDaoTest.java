package com.jbb.mgt.core.dao;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.Mobiles;
import com.jbb.server.common.Home;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class MobilesDaoTest {

    @Autowired
    private MobilesDao mobilesDao;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testMobilesDao() {

        // 先插入数据
        Mobiles mobiles = new Mobiles();
        String phoneNumber = "13612345678";
        mobiles.setPhoneNumber(phoneNumber);
        mobiles.setCheckType("1");
        mobiles.setCheckResult("1");
        mobilesDao.insertMobiles(mobiles);
      
      

           // 插入第二条数据
        mobiles.setPhoneNumber(phoneNumber);
        mobiles.setCheckType("2");
        mobiles.setCheckResult("1");
        mobilesDao.insertMobiles(mobiles);

        // 插入第三条条数据
        mobiles.setPhoneNumber(phoneNumber);
        mobiles.setCheckType("2");
        mobiles.setCheckResult("2");
        mobilesDao.insertMobiles(mobiles);

        String phoneNumber1 = "13612345678";

        List<Mobiles> ll = mobilesDao.selectMobiles(phoneNumber1,null);
        assertTrue(ll.size() == 2);
        List<Mobiles> l2 = mobilesDao.selectMobiles(phoneNumber1,"1");
        assertTrue(l2.size() == 1);
        
        System.out.println("=======================================================================");
        System.out.println(l2);
        System.out.println(ll);

    }

}
