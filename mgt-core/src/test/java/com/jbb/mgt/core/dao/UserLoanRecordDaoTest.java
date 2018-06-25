package com.jbb.mgt.core.dao;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.UserLoanRecord;
import com.jbb.mgt.core.domain.UserLoanRecordDetail;
import com.jbb.server.common.Home;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;

@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class UserLoanRecordDaoTest {

    @Autowired
    private UserLoanRecordDao loanRecordDao;

    @Autowired
    private UserLoanRecordDetailDao loanRecordDetailDao;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testLoanRecordDao() {
        Timestamp ts = DateUtil.getCurrentTimeStamp();
        UserLoanRecord uLoanRecord = new UserLoanRecord();
        uLoanRecord.setAccountId(1);
        uLoanRecord.setUserId(1);
        uLoanRecord.setApplyId(1);
        uLoanRecord.setBorrowingAmount(100000);
        uLoanRecord.setBorrowingDate(ts);
        uLoanRecord.setRepaymentDate(DateUtil.calTimestamp(ts.getTime(), 7 * DateUtil.DAY_MILLSECONDES));
        uLoanRecord.setAnnualRate(1500);
        uLoanRecord.setIouCode(StringUtil.randomAlphaNum(16));
        uLoanRecord.setCreationDate(ts);
        uLoanRecord.setIouPlatformId(1);
        loanRecordDao.insertUserLoanRecord(uLoanRecord);

        // 获取
        UserLoanRecord uLoanRecordR = loanRecordDao.selectUserLoanRecordByLoanId(uLoanRecord.getLoanId());
        assertNotNull(uLoanRecordR);
        assertEquals(uLoanRecordR.getIouCode(), uLoanRecord.getIouCode());

        // 测试更新
        uLoanRecordR.setLoanAccId(1);
        uLoanRecordR.setLoanAmount(80000);
        uLoanRecordR.setLoanDate(DateUtil.getCurrentTimeStamp());
        loanRecordDao.updateUserLoanRecord(uLoanRecordR);

        // 再获取
        UserLoanRecord uLoanRecordR2 = loanRecordDao.selectUserLoanRecordByLoanId(uLoanRecord.getLoanId());
        assertNotNull(uLoanRecordR2);
        assertEquals(uLoanRecordR.getLoanAmount(), uLoanRecordR2.getLoanAmount());
    }

    @Test
    public void testLoanRecordDetailDao() {
        UserLoanRecordDetail rd = new UserLoanRecordDetail(1, 1, 1, 10000, 1);
        loanRecordDetailDao.insertUserLoanRecordDetail(rd);

        List<UserLoanRecordDetail> list = loanRecordDetailDao.selectUserLoanRecordDetails(1);
        assertNotNull(list);
        assertTrue(list.size()>0);
    }

}
