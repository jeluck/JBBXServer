package com.jbb.server.core.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class ProcessUserOnStepOneUtilTest {

    // @Test
    public void testProcessUserOnStepOne() throws InterruptedException {
        ProcessUserOnStepOneUtil.init();

        new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    ProcessUserOnStepOneUtil.sendUserWithDelay(i, 20);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    ProcessUserOnStepOneUtil.sendUserWithDelay(i, 20);
                }

            }
        }).start();

        Thread.sleep(10000);
        new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        int num = (int)(Math.random() * 30);
                        Thread.sleep(num * 1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    ProcessUserOnStepOneUtil.removeSendTask(i);
                }

            }
        }).start();

        Thread.sleep(60 * 60 * 1000);
        // ProcessUserOnStepOneUtil.destroy();
    }
}
