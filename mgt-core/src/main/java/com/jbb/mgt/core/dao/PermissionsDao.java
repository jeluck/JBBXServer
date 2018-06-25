package com.jbb.mgt.core.dao;

import java.util.List;

import com.jbb.mgt.core.domain.Permissions;

/**
 * 权限表 Dao接口
 * 
 * @author wyq
 * @date 2018/04/28
 */
public interface PermissionsDao {

    Permissions selectPermissionsByPermissionsId(Integer PermissionsId);

    List<Permissions> selectPermissions();
}
