package com.jbb.mgt.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.mgt.core.domain.DataFlowSetting;
import com.jbb.mgt.core.domain.Organization;
import com.jbb.mgt.core.domain.Roles;
import com.jbb.server.common.Home;

/**
 * 角色service测试类
 * 
 * @author wyq
 * @date 2018/04/27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class RolesServiceTest {

    @Autowired
    private RolesService rolesService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testDataDlowSettingService() {
        // 获取数据并验证
        List<Roles> list = rolesService.selectRoles();
        assertTrue(list.size() == 11);

        Roles roles = rolesService.selectRolesByRoleId(1);
        assertEquals(list.get(0).getDescription(), roles.getDescription());
        
        List<Integer> list2 = rolesService.selectRolesAndPermissionsByRoleId(list.get(2).getRoleId());
        assertTrue(list.get(2).getRoleId()==list2.get(0));
    }

}
