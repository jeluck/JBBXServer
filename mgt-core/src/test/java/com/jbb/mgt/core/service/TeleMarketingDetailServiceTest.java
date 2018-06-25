package com.jbb.mgt.core.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.TeleMarketingDetail;
import com.jbb.server.common.Home;

/**
 * 批次明细数据service测试类
 * 
 * @author Administrator
 * @date 2018-4-29 14:03:28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class TeleMarketingDetailServiceTest {
    @Autowired
    private TeleMarketingService service;
    
    @Autowired
    private TeleMarketingDetailService dservice;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    @Rollback
    public void testTeleMarketingDetailService() {
        /*ArrayList<TeleMarketingDetail> list = new ArrayList<TeleMarketingDetail>();
        TeleMarketingDetail t = new TeleMarketingDetail();
        t.setBatchId(1);
        t.setTelephone("110");
        list.add(t);
        TeleMarketingDetail t1 = new TeleMarketingDetail();
        t1.setBatchId(2);
        t1.setTelephone("122");
        list.add(t1);
//        service.insertMobiles(2, list);
        service.updateMobile(t1);*/
        
        List<TeleMarketingDetail> list = dservice.selectTeleMarketingDetails(2);
        System.out.println("list++"+list);
    }

}
