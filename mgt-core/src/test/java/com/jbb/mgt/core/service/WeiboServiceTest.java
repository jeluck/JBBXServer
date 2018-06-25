package com.jbb.mgt.core.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class WeiboServiceTest {

    @Autowired
    private WeiboService weiboService;

    @Test
    public void testWeiboShortUrl(){
        String jsonStr = weiboService.shorten("bbb");
        assertNotNull(jsonStr);
    }

}