package com.jbb.server.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Home;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.domain.Billing;
import com.jbb.server.core.domain.BillingDetail;
import com.jbb.server.core.domain.Repayment;
import com.jbb.server.core.domain.UserBillingSummary;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class BillingServiceTest {

    @Autowired
    private BillingService billingService;

    private int userId = 1000000;

    private int userNotExistId = 1111111;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    private Billing createBilling() {
        Billing billing = new Billing();
        billing.setbAmount(2000.00);
        billing.setbDate(DateUtil.getCurrentTimeStamp());
        billing.setDescription("test billing");
        billing.setLoanTypeId(1);
        billing.setName("借贷测试");
        billing.setPlatformId(8);
        billing.setpAmount(2500.00);
        billing.setpDate(DateUtil.calTimestamp(DateUtil.getCurrentTime(), 7 * 24 * 3600 * 1000));
        billing.setRepeats(1);
        billing.setStartNo(1);
        billing.setUserId(userId);
        return billing;
    }

    @Test
    public void testBilling() {
        // create
        Billing billing = createBilling();
        billingService.saveBilling(billing);

        int id1 = billing.getBillingId();
        assertNotEquals(id1, 0);
        // update
        billing.setbAmount(2200.00);
        billing.setpDate(DateUtil.calTimestamp(DateUtil.getCurrentTime(), -1 * 24 * 3600 * 1000));
        billing.setpAmount(2700.00);
        billing.setpDate(DateUtil.calTimestamp(DateUtil.getCurrentTime(), 7 * 24 * 3600 * 1000));
        billingService.saveBilling(billing);

        // get billing
        Billing b3 = billingService.getBilling(userId, id1, false);
        assertNotNull(b3);

        // get billings
        List<Billing> list = billingService.getBillings(userId, true);
        System.out.println(list);
        assertTrue(list.size() > 0);

        // get billings not exist
        Billing b4 = billingService.getBilling(userNotExistId, id1, false);
        assertNull(b4);

        // get billings by date
        Timestamp startDate = DateUtil.calTimestamp(DateUtil.getCurrentTime(), -100L * DateUtil.DAY_MILLSECONDES);
        Timestamp endDate = DateUtil.calTimestamp(DateUtil.getCurrentTime(), -90L * DateUtil.DAY_MILLSECONDES);
        list = billingService.getBillingsByDate(userId, startDate, endDate);
        assertEquals(list.size(), 0);

        startDate = DateUtil.calTimestamp(DateUtil.getCurrentTime(), 1L * DateUtil.DAY_MILLSECONDES);
        endDate = DateUtil.calTimestamp(DateUtil.getCurrentTime(), 14L * DateUtil.DAY_MILLSECONDES);
        list = billingService.getBillingsByDate(userId, startDate, endDate);
        System.out.println(list.size());
        assertTrue(list.size() > 0);

        // update BillingDetail status
        for (Billing b : list) {
            for (BillingDetail bd : b.getDetails()) {
                billingService.updateBillingDetailStatus(userId, bd.getBillingDetailId(), 1);
            }
        }
        list = billingService.getBillingsByDate(userId, startDate, endDate);
        for (Billing b : list) {
            for (BillingDetail bd : b.getDetails()) {
                assertEquals(bd.getStatus(), 1);
            }
        }
        for (Billing b : list) {
            for (BillingDetail bd : b.getDetails()) {
                billingService.updateBillingDetailStatus(userId, bd.getBillingDetailId(), 0);
            }
        }
        list = billingService.getBillingsByDate(userId, startDate, endDate);
        int bdId = 0;
        for (Billing b : list) {
            for (BillingDetail bd : b.getDetails()) {
                bdId = bd.getBillingDetailId();
                assertEquals(bd.getStatus(), 0);
            }
        }

        // insert repayment
        Repayment repayment = new Repayment();
        repayment.setBillingDetailId(bdId);
        repayment.setAmount(2800.00);
        repayment.setRepayDate(DateUtil.getCurrentTimeStamp());
        billingService.saveRepayment(userId, repayment);
    }

    @Test
    public void testGetBillingSummary() {
        UserBillingSummary summary = billingService.getBillingSummary(userId);
        System.out.println(summary);
        assertNotNull(summary);
    }

}