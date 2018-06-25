package com.jbb.mgt.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.YxNotify;
import com.jbb.mgt.core.domain.YxReport;
import com.jbb.mgt.core.domain.YxReportRsp;
import com.jbb.server.common.Home;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.util.MD5;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class YxReportServiceTest {

    @Autowired
    private YxReportService yxReportService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testYxReport() {
        int userId = 1;
        int applyId = 1;
        String taskId = "testTask";
        YxReport reprot = new YxReport(taskId, 1, 1, YxReport.TYPE_JIEDAIBAO, "token");
        yxReportService.saveReportInfo(reprot);

        YxReport report2 = yxReportService.getReport(userId, applyId, taskId, YxReport.TYPE_JIEDAIBAO);
        assertEquals(report2.getToken(), reprot.getToken());

        report2 = yxReportService.getReport(userId, applyId, null, YxReport.TYPE_JIEDAIBAO);
        assertEquals(report2.getToken(), reprot.getToken());

        report2 = yxReportService.getReport(null, null, taskId, YxReport.TYPE_JIEDAIBAO);
        assertEquals(report2.getToken(), reprot.getToken());

        report2 = yxReportService.getReport(null, null, null, YxReport.TYPE_JIEDAIBAO);
        assertNull(report2);
    }

    @Test
    public void getReportFromYx() {
        String token = "";

        YxReportRsp rsp = yxReportService.getReportFormYx(token);
        assertFalse(rsp.isSuccess());

        token = "d8bd53488cf7408ebadefa8bd447cbbb";

        rsp = yxReportService.getReportFormYx(token);
        assertNotNull(rsp);
    }

    @Test
    public void testNotify() {

        String taskId = "10_dW7b_20180504083502_20_1";
        String token = "11a81ef4e5e746bea0634076d9a3869f";
        String orgId = PropertyManager.getProperty("jbb.mgt.yixiang.api.appId");
        String appKey = PropertyManager.getProperty("jbb.mgt.yixiang.api.appKey");
        String sign = MD5.MD5Encode(orgId + appKey + token);
        YxNotify notify = new YxNotify();
        notify.setOrgId(orgId);
        notify.setToken(token);
        notify.setSuccess("true");
        notify.setTaskId(taskId);
        notify.setSign(sign);
        notify.setWebsite(YxReport.TYPE_JIEDAIBAO);
        yxReportService.notify(notify);

        YxReport report = yxReportService.getReport(20, 1, taskId, YxReport.TYPE_JIEDAIBAO);
        assertNotNull(report);
        assertEquals(report.getTaskId(), taskId);

        assertNotNull(report.getData());
    }

    @Test
    public void generateH5Url() {
        String url = yxReportService.generateH5Url(20, 1, YxReport.TYPE_JIEDAIBAO);
        System.out.println(url);
    }

}
