package com.jbb.server.core.dao;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Home;
import com.jbb.server.core.domain.Advertising;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/core-application-config.xml", "/datasource-config.xml" })
public class AdvertisingDaoTest {
	  @Autowired
	    private AdvertisingDao advertisingDao;
	  @BeforeClass
	    public static void oneTimeSetUp() {
	        Home.getHomeDir();
	    }
	  @Test
	    public void testSelectAdvertising() {
		  String platform="IOS";
		  List<Advertising> ats=advertisingDao.selectAdvertising(platform);
		  System.out.println("IOS"+ats.size());
		  assertNotNull(ats);
		  platform="Android";
		  ats=advertisingDao.selectAdvertising(platform);
		  System.out.println("Android"+ats.size());
		  assertNotNull(ats);
		  platform="Web";
		  ats=advertisingDao.selectAdvertising(platform);
		  System.out.println("Web"+ats.size());
		  assertNotNull(ats);
	  }
}
