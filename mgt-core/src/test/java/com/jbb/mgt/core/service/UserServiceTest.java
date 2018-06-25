package com.jbb.mgt.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.core.domain.UserKey;
import com.jbb.server.common.Home;
import com.jbb.server.common.exception.WrongUserKeyException;
import com.jbb.server.common.util.DateUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})

public class UserServiceTest {

    @Autowired
    private UserService userService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    @Rollback
    public void UserTest() {

        // 先插入数据
        User user = new User();
        // String code = StringUtil.randomAlphaNum(5);
        user.setUserName("zhang");
        user.setPhoneNumber("222222");
        user.setIdCard("213213213");
        user.setIpAddress("深圳");
        user.setZhimaScore(50);
        user.setQq("12580");
        user.setWechat("wechat");
        user.setChannelCode("100");
        user.setCreationDate(DateUtil.getCurrentTimeStamp());
        userService.insertUser(user);

        // 获取数据并验证
        User user2 = userService.selectUserById(user.getUserId());

        assertEquals(user.getChannelCode(), user2.getChannelCode());
        assertEquals(user.getUserName(), user2.getUserName());

        // 获取User列表， 返回数据列表
        List<User> list = userService.selectUsers("100", null, null, null, null);
        assertTrue(list.size() == 3);

    }

    @Test
    @Rollback
    public void getUser() {
        User user = userService.selectUser(1, "18675511205");
        assertTrue(user.getUserId() != 0);
    }

    @Test
    public void getUserList() {
        try {
            List<User> list = userService.selectUsers("222444", "1231", "zP6r0", DateUtil.getCurrentTimeStamp(),
                DateUtil.getCurrentTimeStamp());
            assertTrue(list.size() > 0);
        } catch (Exception e) {

        }
    }

    @Test
    public void testUserKey() {

        UserKey userKey = userService.createUserKey(1, 1, 5000000L, true);
        assertNotNull(userKey);

        User user = userService.login(userKey.getUserKey());
        assertNotNull(user);

        userService.logoutUser(userKey.getUserKey());
        try {
            user = userService.login(userKey.getUserKey());
        } catch (WrongUserKeyException ex) {
            assertEquals(ex.getApiErrorCode(), 2);
        }
    }

    @Test
    public void testGetUserDatil() {
        List<User> list = userService.selectUserDetails("5C0237", DateUtil.getCurrentTimeStamp(),
            DateUtil.getCurrentTimeStamp(), 1, false);
        assertTrue(list.size() > 0);
    }
}
