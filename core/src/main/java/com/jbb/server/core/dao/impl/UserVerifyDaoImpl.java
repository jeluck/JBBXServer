package com.jbb.server.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbb.server.common.Constants;
import com.jbb.server.core.dao.UserVerifyDao;
import com.jbb.server.core.dao.mapper.AccountMapper;
import com.jbb.server.core.dao.mapper.UserVerifyMapper;
import com.jbb.server.core.domain.UserVerifyResult;

@Repository("userVerifyDao")
public class UserVerifyDaoImpl implements UserVerifyDao {

    @Autowired
    private UserVerifyMapper mapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUserVerifyResult(int userId, String verifyType, int verifyStep, boolean verified) {
        mapper.saveUserVerifyResult(userId, verifyType, verifyStep, verified);
        //兼容以前设计，如果是实名认证的最后一步，验证通过后，也将原来用户表的验证字段设置为通过验证
        if (Constants.VERIFY_TYPE_REALNAME.equals(verifyType) && verifyStep == 3 && verified) {
            accountMapper.updateUserVerified(userId);
        }

    }

    @Override
    public boolean checkUserVerified(int userId, String verifyType, int verifyStep) {
        return mapper.checkUserVerified(userId, verifyType, verifyStep) > 0;
    }

    @Override
    public List<UserVerifyResult> selectUserVerifyResult(int userId) {
         return mapper.selectUserVerifyResult(userId);
    }
    
    

}
