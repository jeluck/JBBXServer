package com.jbb.server.core.dao;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Home;
import com.jbb.server.common.util.DateUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/core-application-config.xml", "/datasource-config.xml" })
public class UserEventLogDaoTest {
	@Autowired
	private UserEventLogDao userEventLogDao;

	@BeforeClass
	public static void oneTimeSetUp() {
		Home.getHomeDir();
	}

	@Test
	public void insertEventLog() {
		boolean ret = userEventLogDao.insertEventLog(1, "002", "test", "test", "test", "test","ip address", DateUtil.getCurrentTimeStamp());
		 assertTrue(ret);
	}
}
