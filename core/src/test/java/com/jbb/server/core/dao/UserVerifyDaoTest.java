package com.jbb.server.core.dao;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Constants;
import com.jbb.server.common.Home;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserVerifyResult;
import com.jbb.server.core.service.AccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class UserVerifyDaoTest {

    @Autowired
    private UserVerifyDao dao;
    
    @Autowired
    private AccountService accountService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testVerify() {
        int userId = 1000000;
        String verifyType = Constants.VERIFY_TYPE_REALNAME;
        dao.saveUserVerifyResult(userId, verifyType, 1, true);
        boolean verified = dao.checkUserVerified(userId, verifyType, 1);
        assertTrue(verified);
        dao.saveUserVerifyResult(userId, Constants.VERIFY_TYPE_REALNAME, 2, true);
        verified = dao.checkUserVerified(userId, verifyType, 2);
        assertTrue(verified);
        dao.saveUserVerifyResult(userId, Constants.VERIFY_TYPE_REALNAME, 3, true);
        verified = dao.checkUserVerified(userId, verifyType, 3);
        assertTrue(verified);
        
        User user =  accountService.getUser(userId);
        assertTrue(user.isVerified());
        
        List<UserVerifyResult> r = this.accountService.getUserVerifyResults(userId);
        
        r.stream().forEach(ur->System.out.println(ur));
    }

}
