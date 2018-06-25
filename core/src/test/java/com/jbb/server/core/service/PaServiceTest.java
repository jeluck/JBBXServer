package com.jbb.server.core.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Home;
import com.jbb.server.core.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class PaServiceTest {
    @Autowired
    private AccountService accountService;

    @Autowired
    private PaService paService;

    private static int testUserId = 1000000;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testPostData() {
        User user = accountService.getUser(testUserId);
        paService.postUserToPa(user, "127.0.0.2");
    }
}
