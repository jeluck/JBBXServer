package com.jbb.server.core.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Constants;
import com.jbb.server.common.Home;
import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.domain.Iou;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class IousDaoTest {

    @Autowired
    private IousDao iouDao;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    private static int borrowerId = 1000000;
    private static String notExistIouId = "NOTEXISTIOUCODE";
    private static String testPurpose = "助学贷款";

    public static Iou createIou() {
        Iou iou = new Iou();
        iou.setIouCode(StringUtil.generateIoUId());
        iou.setBorrowerId(borrowerId);
        long ts = DateUtil.getCurrentTime();
        long tsR = ts + 7L * DateUtil.DAY_MILLSECONDES;
        iou.setBorrowingDate(new Timestamp(ts));
        iou.setRepaymentDate(new Timestamp(tsR));
        iou.setAnnualRate(Math.random() * 24);
        iou.setBorrowingAmount(Math.random() * 5000);
        iou.setPurpose(testPurpose);
        return iou;
    };

    @Test
    public void testGetNotExist() {
        Iou iou = iouDao.getIouByCode(notExistIouId);
        assertNull(iou);
    }

    @Test
    public void testIou() {
        Iou iou = createIou();
        iouDao.insertIou(iou);

        Iou iou2 = iouDao.getIouByCode(iou.getIouCode());
        assertEquals(iou.getIouCode(), iou2.getIouCode());
        assertTrue(iou.getBorrowerId() == iou2.getBorrowerId());
    }

    @Test
    public void testGetIousForHall() {
        int pageSize = 3;
        List<Iou> list = iouDao.searchIousForHall(null, null, pageSize);
        list.stream().forEach(iou -> System.out.println(iou));

        int cnt = 0;
        while (!CommonUtil.isNullOrEmpty(list) && (cnt++) < 5) {
            String lastIouCode = list.get(list.size() - 1).getIouCode();
            list = iouDao.searchIousForHall(lastIouCode, 0, pageSize);
            list.stream().forEach(iou -> System.out.println(iou));
        }

        System.out.println("==forward:old====");
        cnt = 0;
        String lastIouCode = "JBB201802023922753131";
        list = iouDao.searchIousForHall(lastIouCode, 0, pageSize);
        list.stream().forEach(iou -> System.out.println(iou));
        while (!CommonUtil.isNullOrEmpty(list) && (cnt++) < 5) {
            lastIouCode = list.get(list.size() - 1).getIouCode();
            list = iouDao.searchIousForHall(lastIouCode, 0, pageSize);
            list.stream().forEach(iou -> System.out.println(iou));
        }

        System.out.println("==forward:new====");
        cnt = 0;
        lastIouCode = "JBB201802023922753131";
        list = iouDao.searchIousForHall(lastIouCode, 1, pageSize);
        list.stream().forEach(iou -> System.out.println(iou));
        while (!CommonUtil.isNullOrEmpty(list) && (cnt++) < 5) {
            lastIouCode = list.get(list.size() - 1).getIouCode();
            list = iouDao.searchIousForHall(lastIouCode, 1, pageSize);
            list.stream().forEach(iou -> System.out.println(iou));
        }

    }

    @Test
    public void testGetFolloweredIous() {
        int userId = 1000000;
        List<Iou> list = iouDao.selectFollowedIous(userId);
        list.stream().forEach(iou -> System.out.println(iou));
    }

    @Test
    public void testGetBorrowIous() {
        int userId = 1000000;
        List<Iou> list = iouDao.selectPublishedIous(userId);
        list.stream().forEach(iou -> System.out.println(iou));

    }

    @Test
    public void testGeLendIous() {
        int userId = 1000000;
        List<Iou> list = iouDao.selectLendIous(userId);
        list.stream().forEach(iou -> System.out.println(iou));
    }

    @Test
    public void testCount() {
        int userId = 1000000;
        Timestamp start = null;
        int cnt = iouDao.countFollowUpdatedIous(userId, start);
        System.out.println(cnt);
        cnt = iouDao.countHallNewIous(start);
        System.out.println(cnt);
        cnt = iouDao.countBorrowOrLendUpdatedIous(null, userId, start);
        System.out.println(cnt);
        cnt = iouDao.countBorrowOrLendUpdatedIous(userId, null, start);
        System.out.println(cnt);

    }

    @Test
    public void getGetRecentIous() {
        int userId = 1000001;
        Timestamp currentDate = DateUtil.getCurrentTimeStamp();
        int size = 3;
        int[] statuses = {0, 2, 3, 4};
        List<Iou> ious = iouDao.selectRecentIousByBorrowerId(userId, currentDate, true, size, null, statuses);
        System.out.println(ious);
        ious = iouDao.selectRecentIousByBorrowerId(userId, currentDate, false, size, null, statuses);
        System.out.println(ious);

    }

    @Test
    public void selectIouForMessage() throws ParseException {
        int[] statuses = Constants.IOU_STATUS_EFFECT_WITHOUT_COMPLETED;
        // 还有一天到期的时间范围

        long todayTs = DateUtil.getTodayStartTs();
        long yesterdayBegain = todayTs - 100 * DateUtil.DAY_MILLSECONDES;
        long endTs = todayTs + 2 * DateUtil.DAY_MILLSECONDES;

        Timestamp start = new Timestamp(yesterdayBegain);
        Timestamp end = new Timestamp(endTs);
        int messageType = 4;
        
        Timestamp sendDate = DateUtil.getCurrentTimeStamp();
        List<Iou> ioulist = iouDao.selectIousForMessage(statuses, 9999, messageType, sendDate, start, end);
        ioulist.forEach(iou -> System.out.println(iou));
    }

}
