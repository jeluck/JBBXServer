package com.jbb.server.core.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Home;
import com.jbb.server.core.domain.CompanyIntroduction;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class CompanyIntroductionDaoTest {
    @Autowired
    private CompanyIntroductionDao companyIntroductionDao;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testSelect() {
        CompanyIntroduction companyIntroduction = companyIntroductionDao.selectCompanyIntroduction();
        System.out.println(companyIntroduction);
        assertNotNull(companyIntroduction);
    }
}
