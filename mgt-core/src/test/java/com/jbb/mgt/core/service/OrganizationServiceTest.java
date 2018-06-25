package com.jbb.mgt.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.Organization;
import com.jbb.server.common.Home;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class OrganizationServiceTest {

    @Autowired
    private OrganizationService organizationService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testOrganizations() {
        // 插入org
        Organization org = new Organization("test org 1", "test org desc");
        organizationService.insertOrganization(org);
        int orgId = org.getOrgId();

        // 获取org
        Organization orgR = organizationService.getOrganizationById(orgId);
        assertEquals(org.getDescription(), orgR.getDescription());
        assertEquals(org.getName(), orgR.getName());
        
        //删除org
        organizationService.deleteOrganization(orgId);
        
        // 获取org
        orgR = organizationService.getOrganizationById(orgId);
        assertTrue(orgR.isDeleted());
    
    }

}
