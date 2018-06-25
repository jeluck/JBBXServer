package com.jbb.server.core.service;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.core.domain.Property;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})

public class SystemServiceTest {

    @Autowired
    SystemService systemService;

    @Test
    public void testSystemProperties() {
        int version = systemService.getPropsVersion();
        System.out.println(version);
        systemService.saveSystemProperty("test", "test");
        List<Property> list = systemService.getSystemProperties();

        boolean flag = false;
        for (Property p : list) {
            if ("test".equals(p.getName()) && "test".equals(p.getValue())) {
                flag = true;
            }
        }
        assertTrue(flag);
    }

}
