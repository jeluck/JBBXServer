package com.jbb.mgt.core.service;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.PreloanReport;
import com.jbb.server.common.Home;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class TdRiskServiceTest {
    @Autowired
    private TdRiskService tdRiskService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    //@Test
    public void testTdPreloan() {

        PreloanReport report = tdRiskService.getPreloanReportByApplyId(1);
        assertNotNull(report);
        
        report = tdRiskService.getPreloanReportByReportId("ER20180426163658E7DBFA9C");
        assertNotNull(report);
    }
}
