package com.jbb.mgt.core.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.IouStatus;
import com.jbb.mgt.core.domain.LoanRecordUpdateRsp;
import com.jbb.mgt.core.domain.ReMgtIou;
import com.jbb.mgt.core.domain.UserResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})

public class JbbServiceTest {

    @Autowired
    private JbbService jbbService;

    @Test
    public void testUser() {
        UserResponse user = jbbService.getUserReportByPhoneNumber("1888888888");
        assertTrue(user != null);
    }

    @Test
    public void tetsUpdateUserLoanRecord() {
        IouStatus ret = new IouStatus();
        ret.setIouCode("JBB2018053137492098869");
        ret.setExtentionDate("2018-07-10");
        ret.setStatus(11);
        LoanRecordUpdateRsp loanRecordUpdateRsp = jbbService.updateIouStatus(ret);
        assertNotNull(loanRecordUpdateRsp);
    }

    @Test
    public void testUserCheck() {
        String userName = "唐文华";
        Integer Code = jbbService.check(1000000, userName);
        assertNotNull(Code);
        System.out.println(Code);
    }

    @Test
    public void testMgtToJbb() {
        ReMgtIou m = new ReMgtIou();
        m.setAnnualRate(20);
        m.setBorrowerId(1000000);
        m.setBorrowingAmount(1000);
        m.setBorrowingDate("1527582991474");
        m.setLenderId(1000007);
        m.setRepaymentDate("1623042390428");
        String s = jbbService.fillIou(m);
        assertNotNull(s);
        System.out.print("iouCode=============" + s);

    }
    
    @Test
    public void testUserCheckSend() {
        Integer Code = jbbService.checkSend(1, 1000000);
        assertNotNull(Code);
        System.out.println(Code);
    }
}
