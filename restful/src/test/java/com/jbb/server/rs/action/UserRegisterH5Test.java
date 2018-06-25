package com.jbb.server.rs.action;

import java.sql.Timestamp;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Home;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.dao.MessageCodeDao;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.service.AccountService;
import com.jbb.server.core.util.PropertiesUtil;
import com.jbb.server.core.util.SpringUtil;
import com.jbb.server.mq.MqClient;
import com.jbb.server.mq.Queues;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/core-application-config.xml", "/datasource-config.xml"})
public class UserRegisterH5Test {

    @Autowired
    private MessageCodeDao messageCodeDao;
    
    @Autowired
    private AccountService accountService;

    @BeforeClass
    public static void oneTimeSetUp() {
        PropertiesUtil.init();
        Home.getHomeDir();
        Home.settingsTest();
    }

    @Test
    public void test() {
       
        String msgCode = "2222";
        long phoneNumBegin = 40220000001L;
        long ts = DateUtil.getCurrentTime();
        Timestamp start =  DateUtil.calTimestamp(ts, -300*1000);
        Timestamp end = DateUtil.calTimestamp(start.getTime(), DateUtil.DAY_MILLSECONDES);

        String[] sourceIds = {"011"};
//        String[] sourceIds = {"022"};
        for (int i = 0; i < 20; i++) {
            String phoneNumber = String.valueOf(phoneNumBegin + i);
            messageCodeDao.saveMessageCode(phoneNumber, msgCode, start, end);
            int randIndex = (int)(Math.random() * sourceIds.length);
            User user = accountService.createUser(phoneNumber, phoneNumber , null, msgCode, sourceIds[randIndex], "test");
            
            //发送消息到队列
            int registerUserId = user.getUserId();
            MqClient.send(Queues.USER_REGISTER_QUEUE_ADDR, String.valueOf(registerUserId).getBytes(), 0);
            
           
        }

    }

}
