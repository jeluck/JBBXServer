package com.jbb.mgt.core.service.impl;


import com.jbb.mgt.core.dao.UserContantDao;
import com.jbb.mgt.core.domain.UserContant;
import com.jbb.mgt.core.service.UserContantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UserContantService")
public class UserContantServiceImpl implements UserContantService{

    @Autowired
    private UserContantDao userContantDao;

    @Override
    public void insertOrUpdateUserContant(List<UserContant> userContants) {
        userContantDao.insertOrUpdateUserContant(userContants);
    }

    @Override
    public List<UserContant> selectUserContantByUserId(Integer userId) {
        return userContantDao.selectUserContantByUserId(userId);
    }

    @Override
    public void jbbToMgtInsertOrUpdateUserContant(List<UserContant> userContants,Integer userId) {
        userContantDao.jbbToMgtInsertOrUpdateUserContant(userContants,userId);
    }
}
