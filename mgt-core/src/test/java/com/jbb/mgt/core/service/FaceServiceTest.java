package com.jbb.mgt.core.service;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Home;
import com.jbb.server.common.PropertyManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class FaceServiceTest {

    public static String BUCKET_USER_PHOTOS = "jbb-mgt-user-photos";

    @Autowired
    private AccountService accountsService;

    @Autowired
    private AliyunService aliyunservie;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testDownload() {

        try {
            byte[] imageString = aliyunservie.getImageBytesWithStyle(BUCKET_USER_PHOTOS, "4135852188522551.jpg", null);
            System.out.println("imageString  " + imageString.length);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
