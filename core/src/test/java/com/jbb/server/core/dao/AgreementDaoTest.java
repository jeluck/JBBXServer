package com.jbb.server.core.dao;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Home;
import com.jbb.server.core.domain.Agreement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class AgreementDaoTest {

    public static final int TESTUSERID = 1000000;
    public static final String AGREEMENT = "注册协议";
    public static final String VERSION = "1.0";

    @Autowired
    private AgreementDao agreementDao;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testAdd() {
        agreementDao.insertAgreement(TESTUSERID, AGREEMENT, VERSION);

    }

    @Test
    public void testSearch() {
        List<Agreement> agreements = agreementDao.searchAgreement(TESTUSERID, AGREEMENT, VERSION);
        System.out.println(agreements.get(0));
    }
}
