package com.jbb.mgt.core.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.Permissions;

/**
 * 权限表Mapper接口
 * 
 * @author wyq
 * @date 2018/04/28
 */
public interface PermissionsMapper {

    Permissions selectPermissionsByPermissionsId(@Param(value = "permissionId") Integer PermissionsId);

    List<Permissions> selectPermissions();
}
