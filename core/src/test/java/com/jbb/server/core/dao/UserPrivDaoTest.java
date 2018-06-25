package com.jbb.server.core.dao;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Home;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class UserPrivDaoTest {

    public static final int applyUserId = 1000000;
    public static final int userId = 1000001;

    @Autowired
    private UserPrivDao dao;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testPrivDao() {
        dao.saveUserPriv(applyUserId, applyUserId, "qq", true);
        dao.saveUserPriv(applyUserId, applyUserId, "wechat", true);
        dao.saveUserPriv(applyUserId, applyUserId, "info", true);
        dao.saveUserPriv(applyUserId, applyUserId, "phone", true);

        boolean flag = dao.checkUserPrivByPrivName(applyUserId, applyUserId, "qq");
        assertTrue(flag);
        flag = dao.checkUserPrivByPrivName(applyUserId, applyUserId, "wechat");
        assertTrue(flag);
        flag = dao.checkUserPrivByPrivName(applyUserId, applyUserId, "info");
        assertTrue(flag);
        flag = dao.checkUserPrivByPrivName(applyUserId, applyUserId, "phone");
        assertTrue(flag);

        dao.saveUserPriv(applyUserId, applyUserId, "qq", false);
        dao.saveUserPriv(applyUserId, applyUserId, "wechat", false);
        dao.saveUserPriv(applyUserId, applyUserId, "info", false);
        dao.saveUserPriv(applyUserId, applyUserId, "phone", false);

        flag = dao.checkUserPrivByPrivName(applyUserId, applyUserId, "qq");
        assertFalse(flag);
        flag = dao.checkUserPrivByPrivName(applyUserId, applyUserId, "wechat");
        assertFalse(flag);
        flag = dao.checkUserPrivByPrivName(applyUserId, applyUserId, "info");
        assertFalse(flag);
        flag = dao.checkUserPrivByPrivName(applyUserId, applyUserId, "phone");
        assertFalse(flag);

    }

}
