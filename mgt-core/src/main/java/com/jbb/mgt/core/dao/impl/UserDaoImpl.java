package com.jbb.mgt.core.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbb.mgt.core.dao.UserDao;
import com.jbb.mgt.core.dao.mapper.UserMapper;
import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.core.domain.UserKey;
import com.jbb.server.common.util.DateUtil;

@Repository("UserDao")
public class UserDaoImpl implements UserDao {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void insertUser(User user) {
        userMapper.insertUser(user);
    }

    @Override
    public User selectUserById(int userId) {

        return userMapper.selectUserById(userId);
    }

    @Override
    public User selectJbbUserById(int jbbUserId, int orgId) {
        return userMapper.selectUserByJbbUserId(jbbUserId, orgId);
    }

    @Override
    public List<User> selectUsers(String channelCode, Timestamp startDate, Timestamp endDate) {
        // TODO Auto-generated method stub
        return userMapper.selectUsers(channelCode, startDate, endDate);
    }

    @Override
    public List<User> selectUserDetails(String channelCode, Timestamp startDate, Timestamp endDate, Integer orgId,
        Boolean isGetJbbChannels) {
        // TODO Auto-generated method stub
        return userMapper.selectUserDetails(channelCode, startDate, endDate, orgId, isGetJbbChannels);
    }

    @Override
    public int countUserByChannelCode(String channelCode, Timestamp startDate, Timestamp endDate) {
        return userMapper.countUserByChannelCode(channelCode, startDate, endDate);
    }

    @Override
    public User selectUserByApplyId(int applyId) {
        return userMapper.selectUserByApplyId(applyId);
    }

    @Override
    public User selectUser(int orgId, String phoneNumber) {
        return userMapper.selectUser(orgId, phoneNumber);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public boolean insertUserKey(UserKey userKey) {
        boolean res = false;
        try {
            userMapper.insertUserKey(userKey);
            res = true;
        } catch (DuplicateKeyException e) {
            // key exists
            UserKey key = userMapper.selectUserKeyByUserIdAndAppId(userKey.getUserId(), userKey.getApplicationId());

            if (key != null) {
                if (key.isDeleted()) {
                    userMapper.updateUserKey(userKey);
                    res = true;
                } else {
                    // existing key found
                    userKey.setExpiry(key.getExpiry());
                    userKey.setUserKey(key.getUserKey());
                }
            } else {
                throw e;
            }
        }
        return res;
    }

    @Override
    public boolean deleteUserKey(String userKey) {
        return userMapper.deleteUserKey(userKey) > 0;
    }

    @Override
    public UserKey selectUserKeyByUserIdAndAppId(int userId, int applicationId) {
        return userMapper.selectUserKeyByUserIdAndAppId(userId, applicationId);
    }

    @Override
    public User selectUserByUserKey(String userKey) {
        return userMapper.selectUserByUserKey(userKey, DateUtil.getCurrentTimeStamp());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateUserKey(UserKey userKey) {
        userMapper.updateUserKey(userKey);;
    }

    @Override
    public void deleteUserKeyByUserId(int userId) {
        userMapper.deleteUserKeyByUserId(userId);
    }

    @Override
    public Integer getPushCount(Integer orgId, Integer type) {
        return userMapper.getPushCount(orgId, type);
    }

    @Override
    public List<User> selectUserByPhoneNumber(String phoneNumber) {
        return userMapper.selectUserByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean checkUserApplied(int userId, Timestamp start) {
        return userMapper.checkUserApplied(userId, start) > 0;
    }

    @Override
    public boolean checkUserExistInOrg(int orgId, String phoneNumber, Integer jbbUserId, String idCard) {
        return userMapper.checkUserExistInOrg(orgId, phoneNumber, jbbUserId, idCard) > 0;
    }

}
