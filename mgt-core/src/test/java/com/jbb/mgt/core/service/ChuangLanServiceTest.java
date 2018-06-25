package com.jbb.mgt.core.service;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.ChuangLanPhoneNumberRsp;
import com.jbb.mgt.core.domain.ChuangLanWoolCheckRsp;
import com.jbb.server.common.Home;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class ChuangLanServiceTest {

    @Autowired
    private ChuangLanService chuangLanService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    // @Test
    public void testWoolCheck() {
        String mobile = "18620354020";

        ChuangLanWoolCheckRsp rsp = chuangLanService.woolCheck(mobile, null);

        assertNotNull(rsp);

        assertNotNull(rsp.getData());
        System.out.println(rsp);
    }

  //  @Test
    public void testMobiles() throws IOException {

        File file = new File("/Users/VincentTang/Downloads/txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String s = null;
        List<String> mobiles = new ArrayList<String>();
        
        while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
            String[] arr = s.split(",");
            String mobile = arr[0];
            // System.out.println(arr[0]);
            // System.out.println(arr[1]);

            ChuangLanWoolCheckRsp rsp = chuangLanService.woolCheck(mobile, null);

            if (rsp != null && rsp.getData() != null) {
                System.out.println(mobile + "," + arr[1] + "," + rsp.getData().toCsvString());
            } else {
                System.out.println(mobile + "," + arr[1] + ",error");
            }
        }
        br.close();;

    }
    @Test
    public void testValideMobile() {
       // List<String> mobiles = new ArrayList<String>();
        //mobiles.add("18876002470");
       // mobiles.add("18134032567");
        //mobiles.add("18270715860");
        String mobile = "18876002470";
        ChuangLanPhoneNumberRsp rsp = chuangLanService.validateMobile(mobile);

       // assertNotNull(rsp);
        System.out.println(rsp.getData().getArea());
        //assertNotNull(rsp.getResultObj());
        System.out.println(rsp);
        //System.out.println(rsp.getData().getArea());
        //System.out.println(rsp.getResultObj().get(0).getArea());
    }
    
    @Test
    public void testMsgCode() {
        String phoneNumber = "18565791067";
        String channelCode ="111";
        String signName="【借帮帮】";
        //String signTemplate="你好";
        //chuangLanService.sendCode(phoneNumber, channelCode, signName, signTemplate);
        chuangLanService.sendMsgCode(phoneNumber, channelCode, signName);
        
    }
}
