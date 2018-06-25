package com.jbb.mgt.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.jbb.mgt.core.domain.Bill;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.OrgRecharges;
import com.jbb.server.common.Home;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class OrgRechargesServiceTest {

    @Autowired
    private OrgRechargesService orgRechargesService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testOrgRechargesService() {

        OrgRecharges o = new OrgRecharges();
        int orgId = (int)(Math.random()*50+50);
        o.setOrgId(orgId);
        o.setTotalDataAmount(100);
        o.setTotalSmsAmount(200);
        o.setTotalDataExpense(300);
        o.setTotalSmsExpense(400);
        orgRechargesService.insertOrgRecharges(o);
       

        OrgRecharges  l = orgRechargesService.selectOrgRecharges(orgId);
        assertEquals(o.getOrgId(), l.getOrgId());
        assertEquals(o.getTotalDataAmount(), l.getTotalDataAmount());
        assertEquals(o.getTotalSmsAmount(), l.getTotalSmsAmount());
        assertEquals(o.getTotalDataExpense(), l.getTotalDataExpense());
        assertEquals(o.getTotalSmsExpense(), l.getTotalSmsExpense());
        
        int newTotalDataAmount = 500;
        o.setTotalDataAmount(newTotalDataAmount);
        orgRechargesService.updateOrgRecharges(o);
        OrgRecharges  l1 = orgRechargesService.selectOrgRecharges(orgId);
        assertEquals(o.getTotalDataAmount(), l1.getTotalDataAmount());
        
    }
    
}
