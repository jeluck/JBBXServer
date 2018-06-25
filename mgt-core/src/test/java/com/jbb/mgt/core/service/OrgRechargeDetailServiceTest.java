package com.jbb.mgt.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.OrgRechargeDetail;
import com.jbb.server.common.Home;
import com.jbb.server.common.util.StringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class OrgRechargeDetailServiceTest {

    @Autowired
    private OrgRechargeDetailService orgRechargeDetailService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testRechargeDetailService() {
        OrgRechargeDetail o = new OrgRechargeDetail();
        String tradeNo = StringUtil.randomAlphaNum(5);
        o.setTradeNo(tradeNo);
        o.setAccountId(1);
        o.setOpType(2);
        o.setAmount(100);
        o.setStatus(3);
        orgRechargeDetailService.insertOrgRechargeDetail(o);

        OrgRechargeDetail l = orgRechargeDetailService.selectOrgRechargeDetail(tradeNo);
        assertEquals(o.getTradeNo(), l.getTradeNo());
        assertEquals(o.getAccountId(), l.getAccountId());
        assertEquals(o.getOpType(), l.getOpType());
        assertEquals(o.getAmount(), l.getAmount());
        assertEquals(o.getStatus(), l.getStatus());
        //插入第二条数据
        tradeNo = StringUtil.randomAlphaNum(5);
        o.setTradeNo(tradeNo);
        o.setAccountId(2);
        o.setOpType(2);
        o.setAmount(100);
        o.setStatus(3);
        orgRechargeDetailService.insertOrgRechargeDetail(o);

        List<OrgRechargeDetail> l1 = orgRechargeDetailService.selectOrgRechargeDetailById(1);
        assertTrue(l1.size() == 1);
        
        List<OrgRechargeDetail> l2 = orgRechargeDetailService.selectAllOrgRechargeDetail(1);
        assertTrue(l2.size() == 2);

        int status = 6;
        o.setStatus(status);
        orgRechargeDetailService.updateOrgRechargeDetail(o);
        OrgRechargeDetail l3 = orgRechargeDetailService.selectOrgRechargeDetail(tradeNo);
        assertEquals(o.getStatus(), l3.getStatus());
        
    }

    @Test
    public void  testHandleConsumeSms(){
        String tradeNo = orgRechargeDetailService.handleConsumeSms("5ZiGm", "11111",100);
        OrgRechargeDetail l = orgRechargeDetailService.selectOrgRechargeDetail(tradeNo);
        assertNotNull(l);
    }

}
