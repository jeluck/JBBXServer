package com.jbb.mgt.core.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jbb.mgt.core.domain.StatisticsMoney;
import com.jbb.mgt.core.domain.UserApplyRecord;
import com.jbb.server.common.Home;
import com.jbb.server.common.util.DateUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class UserApplyRecordServiceTest {

    @Autowired
    private UserApplyRecordService userApplyRecordService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testUserApplyRecords() {

        UserApplyRecord userApplyRecord = new UserApplyRecord(1, 1);
        userApplyRecordService.createUserApplyRecord(userApplyRecord);

        List<UserApplyRecord> list = userApplyRecordService.getUserApplyRecords(null, null, 1, 1, null, null, null,
            null, null, null, null, null, null, null);
        assertNotNull(list);

        userApplyRecord.setAssignAccId(18);
        userApplyRecord.setAssingDate(DateUtil.getCurrentTimeStamp());
        userApplyRecord.setStatus(10);
        userApplyRecordService.updateUserAppayRecord(userApplyRecord);

        PageHelper.startPage(1, 3);
        PageHelper.orderBy("c.creation_date desc");
        List<UserApplyRecord> list2 = userApplyRecordService.getUserApplyRecords(null, null, 1, 1, null, null, null,
            null, null, null, null, null, null, null);
        PageInfo<UserApplyRecord> page = new PageInfo<UserApplyRecord>(list2);
        System.out.println(page);;
        list2.forEach(uar -> {
            System.out.println(uar);
        });
    }

    @Test
    public void testGetCount() {
        int count = userApplyRecordService.countUserApplyRecords(1, "zP6r0", DateUtil.getCurrentTimeStamp(),
            DateUtil.getCurrentTimeStamp());

    }

    @Test
    public void testIndexCountData() {
        Timestamp tsStartDate = DateUtil.parseStr("2017-05-09 17:31:38");
        Timestamp tsEndDate = DateUtil.parseStr("2019-05-09 17:31:38");
        StatisticsMoney statisticsMoney = userApplyRecordService.selectIndexCountData(tsStartDate, tsEndDate, 1);
        assertTrue(statisticsMoney != null);
    }

    @Test
    public void testGetJbbChannelCount() {
        Timestamp tsStartDate = DateUtil.parseStr("2018-06-07 00:00:00");
        Timestamp tsEndDate = DateUtil.parseStr("2018-06-08 17:31:38");
        int count = userApplyRecordService.countUserApply(100002, tsStartDate, tsEndDate);
        System.out.println(count);
        assertNotNull(count);
    }
    @Test
    public void testgetUserApplyRecordsByOrgId() {
        Timestamp tsStartDate = DateUtil.parseStr("2018-06-07 00:00:00");
        Timestamp tsEndDate = DateUtil.parseStr("2018-06-08 17:31:38");
        List<UserApplyRecord> list = userApplyRecordService.selectUserApplyRecordsByOrgId(100002, tsStartDate, tsEndDate);
        System.out.println("list============="+list);
        assertNotNull(list);
    }

}
