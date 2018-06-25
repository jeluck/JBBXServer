 package com.jbb.server.core.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Constants;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserProperty;
import com.jbb.server.core.util.SpringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
 public class PushUserServiceTest {
    
    private static String SERVER_PATH = "http://139.196.121.68:8800/channel/push";
    
    @Autowired
    AccountService accountService;

    
//    @Test
    public void testCyPushUser() {
        User user = new User();
        user.setUserName("张三");
        user.setIdCardNo("421123199306035610");
        user.setPhoneNumber("15180177861");
        user.setWechat("helloworld");
       
        List<UserProperty> properites = new ArrayList<UserProperty>();
        properites.add(new UserProperty(Constants.SESAME_CREDIT_SCORE, "660"));
        user.setProperites(properites);
        
        PushUserService cyPushUserService = SpringUtil.getBean("cyPushUserService", PushUserService.class);
        
        user.setSourceId(null);
        cyPushUserService.postUserData(user, SERVER_PATH);
        user.setSourceId("013");
        cyPushUserService.postUserData(user, SERVER_PATH);
        user.setSourceId("014");
        cyPushUserService.postUserData(user, SERVER_PATH);
    }
    
    
    @Test
    public void testCyPushUser2() {
        PushUserService cyPushUserService = SpringUtil.getBean("cyPushUserService", PushUserService.class);
        long start = DateUtil.getCurrentTime() - 7200000;
        List<User> users= accountService.getApplyUsers(1001963, new Timestamp(start), null, true);
        System.out.println(users.size());
        for(User u:users){
            String sesame = u.getPropertyVal(Constants.SESAME_CREDIT_SCORE);
            System.out.println(sesame);
            cyPushUserService.postUserData(u, SERVER_PATH);
        }
    }
    
}
