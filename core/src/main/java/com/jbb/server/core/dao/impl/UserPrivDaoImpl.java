package com.jbb.server.core.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.server.core.dao.UserPrivDao;
import com.jbb.server.core.dao.mapper.UserPrivMapper;

@Repository("UserPrivDao")
public class UserPrivDaoImpl implements UserPrivDao {

    @Autowired
    private UserPrivMapper mapper;

    @Override
    public void saveUserPriv(int applyUserId, int userId, String privName, boolean privValue) {
        mapper.saveUserPriv(applyUserId, userId, privName, privValue);

    }

    @Override
    public boolean checkUserPrivByPrivName(int applyUserId, int userId, String privName) {
        return mapper.checkUserPrivByPrivName(applyUserId, userId, privName) > 0;
    }

}
