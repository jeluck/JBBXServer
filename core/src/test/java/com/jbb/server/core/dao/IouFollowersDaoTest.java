package com.jbb.server.core.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Home;
import com.jbb.server.core.domain.FollowerRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class IouFollowersDaoTest {

    public static final int TESTUSERID = 1000000;
    public static final int TESTUSERID_2 = 1000001;
    public static final String TESTIOUCODE = "JBB2018013131351140244";

    @Autowired
    private IouFollowersDao iouFDao;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testFollowIou() {
        int status = 1;
        iouFDao.saveIouFollower(TESTUSERID, TESTIOUCODE, status);
        FollowerRecord r = iouFDao.getIouFollowerByUserId(TESTIOUCODE, TESTUSERID);
        assertEquals(r.getStatus(), (Integer)status);
        boolean f = iouFDao.checkExistUserFollowed(TESTIOUCODE, TESTUSERID);
        assertTrue(f);

        status = 0;
        iouFDao.saveIouFollower(TESTUSERID, TESTIOUCODE, status);
        r = iouFDao.getIouFollowerByUserId(TESTIOUCODE, TESTUSERID);
        assertEquals(r.getStatus(), (Integer)status);

        f = iouFDao.checkExistUserFollowed(TESTIOUCODE, TESTUSERID);
        assertFalse(f);

        iouFDao.saveIouFollower(TESTUSERID, TESTIOUCODE, 1);
        iouFDao.saveIouFollower(TESTUSERID_2, TESTIOUCODE, 1);

        List<FollowerRecord> list = iouFDao.getIouFollowers(TESTIOUCODE);
        System.out.println(list.size());

    }

}
