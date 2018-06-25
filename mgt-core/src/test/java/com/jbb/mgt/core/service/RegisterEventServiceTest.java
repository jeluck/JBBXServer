package com.jbb.mgt.core.service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.mapper.Mapper;
import com.jbb.mgt.core.domain.mq.RegisterEvent;
import com.jbb.mgt.server.core.util.OrgDataFlowSettingsUtil;
import com.jbb.mgt.server.core.util.PropertiesUtil;
import com.jbb.server.common.Home;
import com.jbb.server.mq.MqClient;
import com.jbb.server.mq.Queues;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class RegisterEventServiceTest {

    @Autowired
    private RegisterEventService registerEventService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
        PropertiesUtil.init();
        OrgDataFlowSettingsUtil.init();
    }

    @AfterClass
    public static void after() {
        OrgDataFlowSettingsUtil.destroy();
        PropertiesUtil.destroy();
    }

    @Test
    public void testProcess() {

        for (int i = 0; i < 1; i++) {
            RegisterEvent event = new RegisterEvent(2, 10010044, "jbbd");
            registerEventService.processEvent(event);
        }

    }

//    @Test
    public void sendRegisterEventToMq() {

        RegisterEvent event = new RegisterEvent(2, 10010044, "jbbd");
        MqClient.send(Queues.JBB_MGT_USER_REGISTER_QUEUE_ADDR, Mapper.toBytes(event), 300000);

    }

}
