package com.jbb.server.core.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Home;
import com.jbb.server.core.domain.AliPolicy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class AliyunServiceTest {

    @Autowired
    private AliyunService aliyunService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void sencCode() {
        //test send test account msgCode
        aliyunService.sendCode("17031147528");
        // aliyunService.sendCode("18575511205");
    }

    @Test
    public void getPostPolicy() {
        AliPolicy policy = aliyunService.getPostPolicy(30, null);
        System.out.println(policy);
        assertNotNull(policy);
    }

    @Test
    public void generatePresignedUrl() {
        //Not Exist user Id
        String url = aliyunService.generatePresignedUrl(1000000, System.currentTimeMillis() + 60 * 1000);
        System.out.println(url);
        assertNull(url);
        //Exist User Id
        url = aliyunService.generatePresignedUrl(30, System.currentTimeMillis() + 600 * 1000);
        System.out.println(url);
        assertNotNull(url);
    }
    
    @Test
    public void testChekcIdCard(){
        aliyunService.checkIdCard(1000000, "430426198606056175", "唐文华");
    }
}
