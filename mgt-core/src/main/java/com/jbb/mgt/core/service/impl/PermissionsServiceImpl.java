package com.jbb.mgt.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.dao.PermissionsDao;
import com.jbb.mgt.core.domain.Permissions;
import com.jbb.mgt.core.service.PermissionsService;

/**
 * 权限表Service实现类
 * 
 * @author wyq
 * @date 2018/04/28
 */
@Service("PermissionsService")
public class PermissionsServiceImpl implements PermissionsService {

    @Autowired
    private PermissionsDao permissionsDao;

    @Override
    public Permissions selectPermissionsByPermissionsId(Integer PermissionsId) {
        return permissionsDao.selectPermissionsByPermissionsId(PermissionsId);
    }

    @Override
    public List<Permissions> selectPermissions() {
        return permissionsDao.selectPermissions();
    }

}
