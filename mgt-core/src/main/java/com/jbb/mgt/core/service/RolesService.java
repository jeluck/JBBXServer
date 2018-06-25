package com.jbb.mgt.core.service;

import java.util.List;

import com.jbb.mgt.core.domain.Roles;

/**
 * 角色service接口类
 * 
 * @author wyq
 * @date 2018/04/28
 */
public interface RolesService {

    List<Roles> selectRoles();

    Roles selectRolesByRoleId(Integer roleId);
    
    List<Integer> selectRolesAndPermissionsByRoleId(Integer roleId);
    
}
