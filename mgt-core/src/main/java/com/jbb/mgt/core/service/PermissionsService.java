package com.jbb.mgt.core.service;

import java.util.List;

import com.jbb.mgt.core.domain.Permissions;

/**
 * 权限表Service接口
 * 
 * @author Administrator
 * @date 2018/04/28
 */
public interface PermissionsService {

    Permissions selectPermissionsByPermissionsId(Integer PermissionsId);

    List<Permissions> selectPermissions();
}
