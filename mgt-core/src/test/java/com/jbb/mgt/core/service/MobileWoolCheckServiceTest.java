 package com.jbb.mgt.core.service;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.ChuangLanWoolCheck;
import com.jbb.server.common.Home;
import com.jbb.server.common.util.StringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
 @ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})

 public class MobileWoolCheckServiceTest {

     @Autowired
     private MobileWoolCheckService mobileWoolCheckService;

     @BeforeClass
     public static void oneTimeSetUp() {
         Home.getHomeDir();
     }

     @Test
     public void testMobileWoolCheckService() {

         // 先插入数据
         ChuangLanWoolCheck check = new ChuangLanWoolCheck();
         String tradeNo = StringUtil.randomAlphaNum(5);
         check.setTradeNo(tradeNo);
         check.setMobile("2243");
         check.setTag("2");
         check.setIpAddrdss("深圳");
         check.setChargesStatus("3");
         check.setStatus("4");
        // check.setUpdateDate();
         mobileWoolCheckService.insertWoolCheckResult(check);
         
         // 获取数据并验证
        ChuangLanWoolCheck check2 = mobileWoolCheckService.getWoolCheckResult("2243");
        assertEquals(check.getTradeNo(), check2.getTradeNo());
        assertEquals(check.getMobile(), check2.getMobile());
        assertEquals(check.getTag(), check2.getTag());
        assertEquals(check.getIpAddrdss(), check2.getIpAddrdss());
        assertEquals(check.getChargesStatus(), check2.getChargesStatus());
        assertEquals(check.getStatus(), check2.getStatus());
     }
}
 
