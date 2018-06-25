 package com.jbb.mgt.core.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.Channel;
import com.jbb.mgt.core.domain.RechargeRecord;
import com.jbb.server.common.Home;

@RunWith(SpringJUnit4ClassRunner.class)
 @ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})

 public class RechargeRecordDaoTest {
    @Autowired
    private RechargeRecordDao rechargeRecordDao;
    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }
    @Test
    public void testRechargeRecordDao() {
        RechargeRecord r = new RechargeRecord();
        r.setTradeNo("8");
        r.setAccountId(2);
        r.setOrgId(3);
        r.setRechargeType("4");
        r.setStatus("5");
        rechargeRecordDao.insertRechargeRecord(r);
        RechargeRecord r2 = rechargeRecordDao.selectRechargeRecord("8");
      
        assertEquals(r.getTradeNo(), r2.getTradeNo());
        assertEquals(r.getAccountId(), r2.getAccountId());
        assertEquals(r.getRechargeType(), r2.getRechargeType());
        assertEquals(r.getOrgId(), r2.getOrgId());
        assertEquals(r.getStatus(), r2.getStatus());
        List<RechargeRecord> l = rechargeRecordDao.selectRechargeRecords(2);
        assertTrue(l.size() == 2);
    }
}
