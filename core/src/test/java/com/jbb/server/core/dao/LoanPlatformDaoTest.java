package com.jbb.server.core.dao;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Home;
import com.jbb.server.core.domain.LoanPlatform;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class LoanPlatformDaoTest {
    @Autowired
    private LoanPlatformDao loanPlatformDao;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testSelectLoanPlatforms() {
        List<LoanPlatform> loans = loanPlatformDao.selectAllLoanPlatforms();
        System.out.println(loans.size());
        assertNotNull(loans);

        Integer userId = null;
        String loanType = null;
        loans = loanPlatformDao.selectLoanPlatforms(userId, loanType);
        System.out.println(loans.size());

        userId = null;
        List<LoanPlatform> pls = loanPlatformDao.selectLoanPlatforms(userId, loanType);
        assertNotNull(pls);
        System.out.println("ALL" + ": " + pls.size());
        loanType = "matched";
        pls = loanPlatformDao.selectLoanPlatforms(userId, loanType);
        System.out.println(loanType + ": " + pls.size());
        assertNotNull(pls);
        loanType = "popular";
        pls = loanPlatformDao.selectLoanPlatforms(userId, loanType);
        System.out.println(loanType + ": " + pls.size());
        assertNotNull(pls);
        loanType = "hot";
        pls = loanPlatformDao.selectLoanPlatforms(userId, loanType);
        System.out.println(loanType + ": " + pls.size());
        assertNotNull(pls);

        userId = 1;
        loanType = null;
        pls = loanPlatformDao.selectLoanPlatforms(userId, loanType);
        assertNotNull(pls);
        System.out.println("ALL" + ": " + pls.size());
        loanType = "matched";
        pls = loanPlatformDao.selectLoanPlatforms(userId, loanType);
        System.out.println(loanType + ": " + pls.size());
        assertNotNull(pls);
        loanType = "popular";
        pls = loanPlatformDao.selectLoanPlatforms(userId, loanType);
        System.out.println(loanType + ": " + pls.size());
        assertNotNull(pls);
        loanType = "hot";
        pls = loanPlatformDao.selectLoanPlatforms(userId, loanType);
        System.out.println(loanType + ": " + pls.size());
        assertNotNull(pls);

    }

}
