package com.jbb.mgt.core.service;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jbb.mgt.core.domain.UserLoanRecord;
import com.jbb.server.common.Home;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class UserLoanRecordServiceTest {
    @Autowired
    private UserLoanRecordService userLoanRecordService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }


    @Test
    @Rollback
    public void testUserLoanRecordService() {

        PageHelper.startPage(1, 10);
        int[] statuses = {1,2,3,4};
        List<UserLoanRecord> list = userLoanRecordService.getUserLoanRecords(null, null, "iou", 1, statuses, null, null,
            null, null, null, null, null);
        PageInfo<UserLoanRecord> pageInfo = new PageInfo<UserLoanRecord>(list);
        
        System.out.println(JSON.toJSONString(pageInfo));
        PageHelper.clearPage();
        

    }

}
