package com.jbb.mgt.core.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.PreloanReport;
import com.jbb.server.common.Home;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})

public class TdPreloanReportDaoTest {

    @Autowired
    private TdPreloanReportDao preloanReportDao;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testTdPreloanReportDao() {

        PreloanReport report = new PreloanReport();
        String reportId = StringUtil.randomAlphaNum(16);
        int userId = 1;
        int applyId = 1;
        report.setApplyId(applyId);
        report.setReq("req test");
        report.setReportId(reportId);
        report.setUserId(userId);
        report.setApplyDate(DateUtil.getCurrentTimeStamp());

        // 插入数据
        preloanReportDao.insertPreloanReport(report);

        // 获取数据, reportId
        PreloanReport reportR = preloanReportDao.selectPreloanReportByApplyId(applyId);
        assertEquals(report.getReq(), reportR.getReq());
        // 获取数据, userId
        reportR = preloanReportDao.selectPreloanReportByApplyId(applyId);
        assertNotNull(reportR);

        // 更新数据
        report.setReportDate(DateUtil.getCurrentTimeStamp());
        report.setFinalScore(89);
        report.setFinalDecision("final decision");
        report.setStatus(1);
        preloanReportDao.updatePreloanReport(report);

        // 获取数据, reportId
        reportR = preloanReportDao.selectPreloanReportByApplyId(applyId);
        assertEquals(report.getFinalScore(), reportR.getFinalScore());
        assertEquals(report.getFinalDecision(), reportR.getFinalDecision());
        assertEquals(report.getStatus(), reportR.getStatus());

    }
}
