package com.jbb.mgt.core.service;

import com.jbb.mgt.core.domain.User;
import com.jbb.server.common.Home;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})

public class UserLoginLogServiceTest {
	
	@Autowired
    private UserLoginLogService userLoginLogService;
	
	@Autowired
    private UserService userService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }
    @Test
    @Rollback
    public void UserLoginLogTest(){
        
        //插入一个用户
        User user = new User();
        user.setIdCard("test");
        user.setUserName("test");
        user.setWechat("wechat");
        userService.insertUser(user);
    
        // 先插入数据
    	Date nowdate=new Date();  
    	SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
    	Timestamp loginDate=Timestamp.valueOf(simpleDate.format(nowdate));  
        userLoginLogService.insertUserLoginLog(user.getUserId(), "shenzheng", loginDate);

//        // 获取数据并验证
//        UserLoginLog userLoginLog=new UserLoginLog();
//        
//        assertEquals(userLoginLog.getUserId(), user.getUserId());
//        assertEquals(userLoginLog.getIpAddress(), "shenzheng");
        //assertEquals(userLoginLog.getLoginDate(), new Timestamp(0));

    }
}
