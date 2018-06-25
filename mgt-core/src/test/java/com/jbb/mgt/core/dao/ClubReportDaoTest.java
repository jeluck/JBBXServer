package com.jbb.mgt.core.dao;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.ClubReport;
import com.jbb.server.common.Home;
import com.jbb.server.common.util.StringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})

public class ClubReportDaoTest {
    @Autowired
    private ClubReportDao clubReportDao;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testClubReport() {
        ClubReport report = new ClubReport();
        report.setChannelType("type");
        report.setRealName("realname");
        report.setChannelAttr("attr");
        report.setChannelSrc("src");
        report.setChannelCode("code");
        report.setCreatedTime("2018-03-24");
        report.setData(null);
        report.setIdentityCode("iindentityCode");
        report.setUsername("username");
        String taskId = StringUtil.randomAlphaNum(16);
        report.setTaskId(taskId);
        // insert report
        clubReportDao.insertReport(report);

        // get Report
        ClubReport report2 = clubReportDao.selectReport(null, null, null, null, null, taskId);
        assertEquals(report.getChannelType(), report2.getChannelType());
    }
}
