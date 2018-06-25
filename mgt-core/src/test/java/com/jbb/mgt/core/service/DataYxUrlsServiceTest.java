package com.jbb.mgt.core.service;

import com.jbb.mgt.core.domain.Channel;
import com.jbb.mgt.core.domain.DataYxUrls;
import com.jbb.server.common.Home;
import com.jbb.server.common.util.StringUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})

public class DataYxUrlsServiceTest {

    @Autowired
    private DataYxUrlsService service;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testDataYxUrlsService() {

        // 先插入数据
        DataYxUrls d = new DataYxUrls();
        d.setUserId(2);
        d.setReportType("2");
        d.setH5Url("www.baidu.com");
        service.insertDataYxUrls(d);

        // 获取数据并验证
        DataYxUrls dy = service.selectDataYxUrlsByUserIdAndReportType(2, "2");
        assertEquals(dy.getH5Url(), d.getH5Url());

    }

    @Test
    public void getChannel() {
        // 先插入数据
        DataYxUrls d = new DataYxUrls();
        d.setUserId(2);
        d.setReportType("2");
        d.setH5Url("www.baidu2.com");
        service.updateDataYxUrls(d);
    }
}
