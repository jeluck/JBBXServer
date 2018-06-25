package com.jbb.mgt.core.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.Roles;

/**
 * 角色Mapper类
 * 
 * @author wyq
 * @date 2018/04/28
 */
public interface RolesMapper {

    List<Roles> selectRoles();

    Roles selectRolesByRoleId(@Param(value = "roleId") Integer roleId);

    List<Integer> selectRolesAndPermissionsByRoleId(@Param(value = "roleId") Integer roleId);

}
