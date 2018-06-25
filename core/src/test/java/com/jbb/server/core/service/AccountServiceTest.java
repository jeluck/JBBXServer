package com.jbb.server.core.service;

import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jbb.server.common.Home;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserProperty;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;
    
    private static int testUserId = 1000000;
    private static int testToUserId = 1000002;
    private static int notExistUserId = 11111111;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }
    
    @Test
    @Transactional
    @Rollback
    public void testUserAccount() {

//        String nickName = "testNickName";
//        String password = "testPassword";
//        String phoneNumber = "124544";
//        String msgCode = "3333";
//        Timestamp creationDate = DateUtil.getCurrentTimeStamp();
//        Timestamp expireDate = DateUtil.calTimestamp(creationDate.getTime(), 5 * 60 * 1000);
//        messageCodeDao.saveMessageCode(phoneNumber, msgCode, creationDate, expireDate);
//        User user = accountService.createUser(phoneNumber, nickName, password, msgCode);
//        assertNotNull(user);

    }
    
  

    /*@Test
    @Transactional
    @Rollback
    public void deleteUserProperty(){
    	List<UserProperty> list = new ArrayList<UserProperty>();
        UserProperty properties = new UserProperty("蚂蚁花呗","5000");
        list.add(properties);
    	accountService.deleteUserProperties(testUserId, list);
    	List<UserProperty> list2 = accountService.searchUserPropertiesByUserIdAndName(testUserId,properties.getName());
    	assertTrue(list2.size()==0);
    }*/
    
    @Test
    public void searchUserPropertiesByUserId(){
        List<UserProperty> userpropertiesList =   accountService.searchUserProperties(testUserId, null);
        assertNotNull(userpropertiesList);
    }

    @Test
    public void searchUserPropertiesByUserIdAndName(){
        accountService.searchUserPropertiesByUserIdAndName(testUserId,"蚂蚁花呗");
    }
    
    
    @Test
    public void getUserByDate(){
        Timestamp start = new Timestamp(DateUtil.getCurrentTime() - 365L*DateUtil.DAY_MILLSECONDES);
        Timestamp end = new Timestamp(DateUtil.getCurrentTime());
        
        List<User> users  = accountService.getApplyUsers(testToUserId, start, end, true);
        System.out.println(users.size());
        
        for(User u :users){
            System.out.println(u);
        }
        assertNotNull(users);
    }
}
