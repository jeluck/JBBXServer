package com.jbb.mgt.core.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.Account;
import com.jbb.mgt.core.domain.AccountKey;
import com.jbb.server.common.Home;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})

public class AccountDaoTest {
    @Autowired
    private AccountDao accountDao;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    @Rollback
    public void testAccountDao() {
        // 插入数据
        // Account account = new Account();
        // int accountId = 1;
        // account.setAccountId(accountId);
        // account.setNickname("AAAAA");
        // account.setOrgId(1);
        // accountDao.insertAccount(account);
        // 通过orgId获取账号信息
        // int orgId = 1;
        // List<Account> accounts = accountDao.selectAccounts(orgId);
        // System.out.println(accounts.size());
        //
        // account = accountDao.selectAccountById(account.getAccountId());
        // String newName = "TestBBccc";
        // account.setNickname(newName);
        // account.setPhoneNumber("111111");
        // // Roles roles= new Roles();
        // // accountId = 6;
        // int[] roles = accountDao.selectAccountRoles(6);
        // for (int j = 0; j < roles.length; j++) {
        // System.out.println(roles[j]);
        // }
        // accountDao.deleteAccountRoles(6);
        //
        // accountDao.updateAccountRoles(6, roles);

        // accountDao.updateAccountRoles(accountId, roles[i]);
        // accountDao.updateAccount(accountId,account);
        // 删除数据
        // accountId = 1;
        // accountDao.deleteAccount(accountId);

    }

    @Test
    public void testUserKey() {
        int accountId = 1;
        AccountKey userKey = new AccountKey();
        userKey.setAccountId(accountId);
        userKey.setUserKey(StringUtil.randomAlphaNum(64));
        long ts = DateUtil.getCurrentTime() + DateUtil.DAY_MILLSECONDES;
        userKey.setExpiry(new Timestamp(ts));

        // 插入UserKey
        accountDao.insertAccountKey(userKey);

        // 获取userKey
        AccountKey userKey2 = accountDao.getAccountKey(accountId);
        assertEquals(userKey.getUserKey(), userKey2.getUserKey());

        // 更新UserKey
        userKey.setUserKey(StringUtil.randomAlphaNum(64));
        ts = DateUtil.getCurrentTime() + DateUtil.DAY_MILLSECONDES;
        userKey.setExpiry(new Timestamp(ts));
        accountDao.updateAccountKey(userKey);

        // 获取userKey
        userKey2 = accountDao.getAccountKey(accountId);
        assertEquals(userKey.getUserKey(), userKey2.getUserKey());

        // 删除userKey
        accountDao.deleteAccountKey(userKey.getUserKey());

        // 获取userKey
        userKey2 = accountDao.getAccountKey(accountId);
        assertTrue(userKey2.isDeleted());

    }

    @Test
    public void testSelectByKey() {
        int accountId = 1;
        AccountKey userKey = new AccountKey();
        String keyStr = StringUtil.randomAlphaNum(64);
        userKey.setAccountId(accountId);
        userKey.setUserKey(keyStr);
        long ts = DateUtil.getCurrentTime() + DateUtil.DAY_MILLSECONDES;
        userKey.setExpiry(new Timestamp(ts));

        // 插入UserKey
        accountDao.insertAccountKey(userKey);
        
        //获取对象
        Account account = accountDao.authenticate(keyStr);

        //验证结论
        assertNotNull(account);
        assertTrue(account.getAccountId() == accountId);
    }
    
    @Test
    public void getOrgAdmin(){
        Account account = accountDao.selectOrgAdminAccount(1);
        assertNotNull(account);
    }
}
