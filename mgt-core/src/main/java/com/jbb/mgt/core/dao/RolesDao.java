package com.jbb.mgt.core.dao;

import java.util.List;

import com.jbb.mgt.core.domain.Roles;

/**
 * 角色Dao接口
 * 
 * @author wyq
 * @date 2018/04/28
 */
public interface RolesDao {

    List<Roles> selectRoles();

    Roles selectRolesByRoleId(Integer roleId);

    List<Integer> selectRolesAndPermissionsByRoleId(Integer roleId);

}
