package com.jbb.mgt.core.dao;

import com.jbb.mgt.core.dao.mapper.UserApplyRecordMapper;
import com.jbb.mgt.core.domain.Statistics;
import com.jbb.mgt.core.domain.UserApplyRecord;
import com.jbb.server.common.Home;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.shared.rs.Util;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author jarome
 * @date 2018/4/29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})

public class UserApplyRecordDaoTest {

    @Autowired
    private UserApplyRecordMapper userApplyRecordMapper;
    @Autowired
    private UserApplyRecordDao userApplyRecordDao;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testUpdateSelected() {

    }

    @Test
    public void testAssignAcc() {
        List<Integer> userIds = new ArrayList<>();
        userIds.add(1);
        userIds.add(26);
        //  userApplyRecordDao.updateAssignAcc(1,2,userIds);
    }

    @Test
    public void testIndexCount() {
        Timestamp tsStartDate = DateUtil.parseStr("2017-05-09 17:31:38");
        Timestamp tsEndDate = DateUtil.parseStr("2019-05-09 17:31:38");
        Statistics s = userApplyRecordDao.selectUserAppayRecordsCountByApplyId(1, tsStartDate, tsEndDate);
        assertTrue(s != null);
    }
}
