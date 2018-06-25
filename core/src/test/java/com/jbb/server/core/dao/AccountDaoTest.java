package com.jbb.server.core.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jbb.server.common.Constants;
import com.jbb.server.common.Home;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserKey;
import com.jbb.server.core.domain.UserProperty;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class AccountDaoTest {
    @Autowired
    private AccountDao accountDao;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    private static int testUserId = 1000000;
    private static int notExistUserId = 11111111;

    public static User getTestUser(AccountDao accountDao) {
        return accountDao.getUser(testUserId);
    }

    @Test
    @Transactional
    @Rollback
    public void testUserKey() {
        User usr = getTestUser(accountDao);

        if (usr == null)
            return;

        int applicationId = -1;
        int userId = usr.getUserId();
        String testUserKey = "testUserKey" + userId + "-" + applicationId;
        UserKey userKey = accountDao.getUserKey(userId, applicationId, UserKey.EMPTY_OAUTH_CLIENT_ID);

        if (userKey != null) {
            userKey.setUserKey(testUserKey);
            userKey.setExpiry(Constants.LAST_SYSTEM_TIMESTAMP);
            userKey.setDeleted(false);
            accountDao.updateUserKey(userKey);
        } else {
            userKey = new UserKey();
            userKey.setUserKey(testUserKey);
            userKey.setExpiry(Constants.LAST_SYSTEM_TIMESTAMP);
            userKey.setUserId(userId);
            userKey.setApplicationId(applicationId);
            userKey.setOauthClientId(UserKey.EMPTY_OAUTH_CLIENT_ID);

            accountDao.insertUserKey(userKey);
        }

        userKey = accountDao.getUserKey(userId, applicationId, UserKey.EMPTY_OAUTH_CLIENT_ID);
        assertNotNull("User key not found", userKey);
        assertEquals(testUserKey, userKey.getUserKey());
        assertTrue(Constants.LAST_SYSTEM_TIMESTAMP.equals(userKey.getExpiry()));
        assertFalse(userKey.isDeleted());

        User user = accountDao.authenticate(testUserKey);
        assertNotNull("Cannot authenticate root user using key", user);
        assertEquals(userId, user.getUserId());

        assertTrue(accountDao.deleteUserKey(testUserKey));

        user = accountDao.authenticate(testUserKey);
        assertNull(user);

        accountDao.deleteAllUserKeys(userId);

        userKey = accountDao.getUserKey(userId, applicationId, UserKey.EMPTY_OAUTH_CLIENT_ID);
        assertNotNull(userKey);
        assertEquals(testUserKey, userKey.getUserKey());
        assertTrue(Constants.LAST_SYSTEM_TIMESTAMP.equals(userKey.getExpiry()));
        assertTrue(userKey.isDeleted());
    }

    @Test
    @Transactional
    @Rollback
    public void testUser() {
        String nickName = "testNickName";
        String password = "testPassword";
        String phoneNumber = "1234123444";
        Timestamp creationDate = DateUtil.getCurrentTimeStamp();
        User userO = new User(phoneNumber, password, nickName, creationDate.getTime());
        boolean ret = accountDao.insertUserBasicInfo(userO);
        assertTrue(ret);
        System.out.println(userO.getUserId());
        User user = accountDao.getUserByPhoneNumber(phoneNumber);
        assertEquals(user.getPhoneNumber(), phoneNumber);
        assertEquals(user.getNickName(), nickName);
        assertEquals(user.getPassword(), password);
        String newPassword = "newTestPassword";
        int userId = user.getUserId();
        accountDao.updateUserPassword(userId, newPassword);
        user = accountDao.getUser(userId);
        assertEquals(user.getPassword(), newPassword);

        ret = accountDao.checkUserSamePhoneNumber(phoneNumber);
        assertTrue(ret);

        String avatarPic = "avatarPic";
        int creditNumber = 660;
        int identityType = 2;
        user.setAvatarPic(avatarPic);
        user.setCreditNumber(creditNumber);
        user.setIdentityType(identityType);
        accountDao.updateUser(user);
        accountDao.updateUserVerified(userId);
        User newUser = accountDao.getUser(userId);
        assertEquals(newUser.getAvatarPic(), avatarPic);
        assertEquals(newUser.getCreditNumber(), creditNumber);
        assertEquals(newUser.getIdentityType(), identityType);
        assertTrue(newUser.isVerified());
    }

    @Test
    @Transactional
    public void testUpdateUser() {
        User user = getTestUser(accountDao);

        user.setUserName("测试名1");
        user.setEmail("test@qq.com");
        user.setIdCardNo("123451234132131111");
        user.setIdentityType(1);
        user.setPhoneServicePassword("it's me");
        user.setBankName("测试银行名称");
        user.setBankCardNo("121313414515616");
        user.setCreditNumber(456789);
        user.setAvatarPic("2logo.png");
        user.setVerified(true);
        user.setNickName("这是测试昵称");
        user.setSex("男");
        user.setAge(20);
        user.setNation("汉");
        user.setIdcardAddress("中国");
        user.setWechat("ikmnhu");
        user.setQqNumber("qqtestnumber");
        user.setAddressBookNumber(21);
        user.setPhoneAuthenticationTime("24hour");
        user.setMarried(true);
        user.setLiveAddress("广东");
        user.setParentAddress("深圳");
        user.setEducation("教授");
        user.setOccupation("教师");
        user.setUserId(testUserId);
        accountDao.updateUser(user);

        User user2 = getTestUser(accountDao);
        assertEquals(user.getAge(), user2.getAge());
        assertEquals(user.isMarried(), user2.isMarried());
    }

    @Test
    public void testSelectUserPropertiesByUserIdAndName() {
        UserProperty property = accountDao.selectUserPropertyByUserIdAndName(1, "蚂蚁花呗");
        System.out.println(property);
    }

    @Test
    public void testUserProperties() {
       
        boolean result = accountDao.insertUserProperty(testUserId, "蚂蚁花呗", "10000");
        assertTrue(result);
        List<UserProperty> properties = accountDao.selectUserProperties(testUserId, null);
        assertNotNull(properties);
        result = accountDao.updateUserPropertyByUserIdAndName(testUserId, "蚂蚁花呗", "2000");
        assertTrue(result);
        result = accountDao.deleteUserPropertyByUserIdAndName(testUserId, "蚂蚁花呗");
        assertTrue(result);

        properties = accountDao.selectUserProperties(notExistUserId, null);
        assertEquals(0, properties.size());

    }

    
    @Test
    public void testGetUserIds(){
        List<Integer> userIds = new ArrayList<Integer>();
        userIds.add(1);
        userIds.add(2);
        userIds.add(3);
        List<User> list = accountDao.getUsers(userIds, null, false);
        for (User user : list) {
            System.out.println(user);
        }
    }
    
    @Test
    public void getUserByDate(){
        Timestamp start = new Timestamp(DateUtil.getCurrentTime() - 365L*DateUtil.DAY_MILLSECONDES);
        Timestamp end = new Timestamp(DateUtil.getCurrentTime());
        
        List<User> users  = accountDao.getApplyUsers(testUserId, start, end, true);
        System.out.println(users.size());
        assertNotNull(users);
    }
}
