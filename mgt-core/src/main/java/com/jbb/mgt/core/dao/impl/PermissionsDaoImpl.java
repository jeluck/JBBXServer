package com.jbb.mgt.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.PermissionsDao;
import com.jbb.mgt.core.dao.mapper.PermissionsMapper;
import com.jbb.mgt.core.domain.Permissions;

/**
 * 权限表Dao实现类
 * 
 * @author wyq
 * @date 2018/04/28
 */
@Repository("PermissionsDao")
public class PermissionsDaoImpl implements PermissionsDao {

    @Autowired
    private PermissionsMapper mapper;
    
    @Override
    public Permissions selectPermissionsByPermissionsId(Integer PermissionsId) {
        return mapper.selectPermissionsByPermissionsId(PermissionsId);
    }

    @Override
    public List<Permissions> selectPermissions() {
        return mapper.selectPermissions();
    }

}
