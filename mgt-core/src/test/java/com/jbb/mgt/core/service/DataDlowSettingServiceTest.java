package com.jbb.mgt.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.DataFlowSetting;
import com.jbb.mgt.core.domain.Organization;
import com.jbb.server.common.Home;

/**
 * 流量控制service测试类
 * 
 * @author Administrator
 * @date 2018/04/27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class DataDlowSettingServiceTest {

    @Autowired
    private DataFlowSettingService dfsService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testDataDlowSettingService() {

        // 先插入数据
        DataFlowSetting dataFlowSetting = new DataFlowSetting();
        int orgId = 1;
        dataFlowSetting.setOrgId(orgId);
        dataFlowSetting.setDataFlowId(1);
        dataFlowSetting.setMinValue(1);
        dataFlowSetting.setMaxAvlue(100);
        dfsService.saveDataFlowSetting(dataFlowSetting);

        // 获取数据并验证
        DataFlowSetting dfsByOid = dfsService.getDataFlowSettingByOrgId(orgId);
        assertEquals(dfsByOid.getOrgId(), dataFlowSetting.getOrgId());
        assertEquals(dfsByOid.getDataFlowId(), dataFlowSetting.getDataFlowId());
        assertEquals(dfsByOid.getMinValue(), dataFlowSetting.getMinValue());
        assertEquals(dfsByOid.getMaxAvlue(), dataFlowSetting.getMaxAvlue());

        // 准备修改数据
        dataFlowSetting.setMinValue(2);
        dfsService.saveDataFlowSetting(dataFlowSetting);
        DataFlowSetting dfsByOid2 = dfsService.getDataFlowSettingByOrgId(orgId);
        assertEquals(dfsByOid2.getOrgId(), dataFlowSetting.getOrgId());
        assertEquals(dfsByOid2.getDataFlowId(), dataFlowSetting.getDataFlowId());
        assertEquals(dfsByOid2.getMinValue(), dataFlowSetting.getMinValue());
        assertEquals(dfsByOid2.getMaxAvlue(), dataFlowSetting.getMaxAvlue());

       
    }

}
