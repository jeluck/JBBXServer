package com.jbb.mgt.core.service;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.DataFlowBase;
import com.jbb.server.common.Home;

/**
 * 流量基本信息service测试类
 * 
 * @author Administrator
 * @date 2018/04/27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class DataFlowBaseServiceTest {
    @Autowired
    private DataFlowBaseService service;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    @Rollback
    public void testDataFlowConfigService() {

        List<DataFlowBase> list = service.getDataFlowBases();
        assertNotNull(list);
    }

}
