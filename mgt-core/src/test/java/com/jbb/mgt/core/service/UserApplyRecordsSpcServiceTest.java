package com.jbb.mgt.core.service;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.UserApplyRecordsSpc;
import com.jbb.server.common.Home;

/**
 * 账号操作日志service,dao测试类
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class UserApplyRecordsSpcServiceTest {

    @Autowired
    private UserApplyRecordsSpcService service;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testUserApplyRecordsSpcService() {
        // 插入数据
       service.insertUserApplay(1111111, 100002);
       int s = service.countApplies(null,null,null);
       List<UserApplyRecordsSpc> l = service.selectAppliesByAccountId(null, null, null);
    }
}
