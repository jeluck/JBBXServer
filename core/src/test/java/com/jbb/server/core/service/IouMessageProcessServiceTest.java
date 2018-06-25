package com.jbb.server.core.service;

import java.sql.Timestamp;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Home;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.domain.Iou;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class IouMessageProcessServiceTest {

    @Autowired
    private IouMessageProcessService iouMessageProcessService;

    @Autowired
    private IousService iouService;
    
    @Autowired
    private IouStatusService iouStatusService;
    

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    private static int borrowerId = 1000000;
    private static int lenderId = 1000007;
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
        iou.setLenderId(lenderId);
        iou.setStatus(1);
        return iou;
    };

    @Test
    public void testMessage() {
        int userId = borrowerId;
        Iou iou = createIou();
        iouService.insertIou(iou);

        // iou.setStatus(30);
        // iouMessageProcessService.sendIouMessage(userId, iou);

        iouStatusService.updateStatus(iou, 5, userId, null, null);
        iouStatusService.updateStatus(iou, 6, lenderId, null, null);
        iouStatusService.updateStatus(iou, 5, userId, null, null);
        iouStatusService.updateStatus(iou, 7, lenderId, null, null);
        iouStatusService.updateStatus(iou, 8, userId, null, null);
        iouStatusService.updateStatus(iou, 10, lenderId, null, null);
        iouStatusService.updateStatus(iou, 8, userId, null, null);
        iouStatusService.updateStatus(iou, 9, lenderId, null, null);
     
        
        for (int i = 1; i <= 31; i++) {
            //iou.setStatus(i);
            
         
            // iouMessageProcessService.sendIouMessage(userId, iou);

//            new Thread(() -> {
//                iouMessageProcessService.sendIouMessage(userId, iou);
//            }).start();
        }

    }

}
