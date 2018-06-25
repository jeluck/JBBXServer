package com.jbb.server.core.service;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Constants;
import com.jbb.server.common.Home;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.domain.Iou;
import com.jbb.server.core.domain.IousAmountStatistic;
import com.mysql.fabric.xmlrpc.base.Array;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class IousServiceTest {

    @Autowired
    private IousService iouService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    // @Test
    // public void testIouFollowers() {
    // iouService.changeIouFollowersStatus(1000003, "TESTIOU201801201218");
    // }
    // @Test
    // public void testIouIntentional() {
    // iouService.changeIouIntentionalUsersStatus(1000003, "TESTIOU201801201218");
    // }
    // @Test
    // public void testRejectIntentional() {
    // List<Integer> list = new ArrayList<Integer>();
    // list.add(1000003);
    // list.add(1000000);
    // iouService.rejectIntention(list, "TESTIOU201801201218");
    // }

    @Test
    public void testGetIou() {

    }

    @Test
    public void getRecnetIous() {
        List<Iou> ious = iouService.getRecentIousForBorrower(1000001);
        System.out.println(ious);
    }

    @Test
    public void iousStatistic() {
        IousAmountStatistic s =
            iouService.statisticIousAmountAndCnt(Constants.IOU_STATUS_EFFECT, 1000000, null, null, null);
        assertNotNull(s);
        System.out.println(s);

        s = iouService.statisticIousAmountAndCnt(Constants.IOU_STATUS_EFFECT, null, 1000000, null, null);
        assertNotNull(s);
        System.out.println(s);

        Timestamp start = DateUtil.getCurrentTimeStamp();
        Timestamp end = DateUtil.calTimestamp(start.getTime(), 30 * DateUtil.DAY_MILLSECONDES);

        s = iouService.statisticIousAmountAndCnt(Constants.IOU_STATUS_EFFECT, 1000000, null, start, end);
        assertNotNull(s);
        System.out.println(s);

    }

    @Test
    public void testSelectIous() {
        int[] filters= {1,3};
        List<Iou> ious = iouService.selectIous(Constants.IOU_STATUS_EFFECT, filters, null,  "JBB201803068586578435",
            null, null, null, 1000000, null, null, null, null, null, null, null, 1, 20);
        
        ious.stream().forEach(iou -> {
            System.out.println("================");
            System.out.println(iou);
        });
        
        int[] filters2= {};
        iouService.selectIous(null,filters2, "Vincent",  
            null, null, null, null, 1000000, null, null, null, null, null, null, null, 1, 20);
        
        ious.stream().forEach(iou -> {
            System.out.println("================");
            System.out.println(iou);
        });
    }
}
