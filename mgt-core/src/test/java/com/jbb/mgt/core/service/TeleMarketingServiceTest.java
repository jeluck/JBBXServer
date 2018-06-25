package com.jbb.mgt.core.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.TeleMarketing;
import com.jbb.mgt.core.domain.TeleMarketingDetail;
import com.jbb.mgt.core.domain.TeleMarketingInit;
import com.jbb.server.common.Home;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * 电销批次service测试类
 * 
 * @author Administrator
 * @date 2018-4-29 14:03:28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class TeleMarketingServiceTest {
    @Autowired
    private TeleMarketingService service;

    @Autowired
    private TeleMarketingService teleService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testTeleMarketingBatch() {
        /*List<TeleMarketing> selectTeleMarketings = service.selectTeleMarketings(1);
        System.out.println("selectTeleMarketings====="+selectTeleMarketings);*/

        /* ArrayList<TeleMarketingInit> list = new ArrayList<TeleMarketingInit>();
        TeleMarketingInit m = new TeleMarketingInit();
        m.setId(3);
        list.add(m);
        TeleMarketingInit m2 = new TeleMarketingInit();
        m2.setId(4);
        list.add(m2);
        service.insertTeleInits(list);*/

        /*List<TeleMarketingInit> list = service.selectTeleInits(1, true, false);
        System.out.println("TeleMarketingInit_list=========="+list);*/

        /*TeleMarketing teleMarketing = new TeleMarketing();
        teleMarketing.setAccountId(1);
        service.insertTeleMarketing(teleMarketing);
        System.out.println("======================================================");
        System.out.println(teleMarketing.getBatchId());*/

        /*
        List<TeleMarketingDetail> list = service.selectMobilesByAccountId(1);
        System.out.println("TeleMarketingDetail_list======"+list);*/

        /*service.updateTeleMarketingInitByIdAndOpCommet(1,"初审");*/
        TeleMarketingInit teleMarketingInit = teleService.selectTeleMarketingInitById(1);
        assertNotNull(teleMarketingInit);

    }

}
