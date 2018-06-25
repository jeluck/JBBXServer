package com.jbb.mgt.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.RolesDao;
import com.jbb.mgt.core.dao.mapper.RolesMapper;
import com.jbb.mgt.core.domain.Roles;

/**
 * 角色Dao实现类
 * 
 * @author wyq
 * @date 2018/04/28
 */
@Repository("RolesDao")
public class RolesDaoImpl implements RolesDao {

    @Autowired
    private RolesMapper mapper;

    @Override
    public List<Roles> selectRoles() {
        return mapper.selectRoles();
    }

    @Override
    public Roles selectRolesByRoleId(Integer roleId) {
        return mapper.selectRolesByRoleId(roleId);
    }

    @Override
    public List<Integer> selectRolesAndPermissionsByRoleId(Integer roleId) {
        return mapper.selectRolesAndPermissionsByRoleId(roleId);
    }

}
