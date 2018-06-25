package com.jbb.server.core.dao.impl;

import com.jbb.server.core.domain.UserProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.dao.AccountDao;
import com.jbb.server.core.dao.mapper.AccountMapper;
import com.jbb.server.core.domain.Role;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserKey;

import java.sql.Timestamp;
import java.util.List;

@Repository("AccountDao")
public class AccountDaoImpl implements AccountDao {

    @Autowired
    private AccountMapper mapper;

    @Override
    // @Cacheable(cacheNames=CACHE_CONSTANTS, keyGenerator="methodCacheKeyGenerator")
    public Role getRole(int roleId) {
        return mapper.selectRoleById(roleId);
    }

    @Override
    // @Cacheable(cacheNames=CACHE_CONSTANTS, keyGenerator="methodCacheKeyGenerator")
    public int[] getRolePermissions(int roleId) {
        List<Integer> perms = mapper.selectRolePermissions(roleId);

        int[] res = null;
        if (perms != null) {
            res = new int[perms.size()];
            for (int i = 0; i < res.length; i++) {
                res[i] = perms.get(i);
            }
        }

        return res;
    }

    @Override
    // @Cacheable(cacheNames=CACHE_CONSTANTS, keyGenerator="methodCacheKeyGenerator")
    public Role[] getAllRoles() {
        List<Role> roles = mapper.selectAllRoles();
        return roles.toArray(new Role[roles.size()]);
    }

    @Override
    public List<UserProperty> selectUserProperties(int userId, String name) {
        return mapper.selectUserProperties(userId, name);
    }

    @Override
    public UserProperty selectUserPropertyByUserIdAndName(int userId, String name) {
        return mapper.selectUserPropertyByUserIdAndName(userId, name);
    }

    @Override
    public boolean deleteUserPropertyByUserIdAndName(int userId, String name) {
        return mapper.deleteUserPropertyByUserIdAndName(userId, name) > 0;
    }

    @Override
    public boolean updateUserPropertyByUserIdAndName(int userId, String name, String value) {
        return mapper.updateUserPropertyByUserIdAndName(userId, name, value) > 0;
    }

    @Override
    public boolean insertUserProperty(int userId, String name, String value) {
        return mapper.insertUserProperty(userId, name, value) > 0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void insertUserProperties(int userId, List<UserProperty> properties) {
        if (CommonUtil.isNullOrEmpty(properties)) {
            return;
        }
        for (UserProperty p : properties) {
            insertUserProperty(userId, p.getName(), p.getValue());
        }

    }

    @Override
    // @Cacheable(cacheNames=CACHE_USERS_BY_KEY)
    public User authenticate(String userKey) {
        return mapper.selectUserByKey(userKey, DateUtil.getCurrentTimeStamp());
    }

    @Override
    public User getUser(int userId) {
        return mapper.selectUserByUserId(userId);
    }

    @Override
    public UserKey getUserKey(int userId, int applicationId, String oauthClientId) {
        return mapper.selectUserKey(userId, applicationId, oauthClientId);
    }

    /**
     * Insert a new user key record and return true. If the key exists and valid, get it properties and return false. If
     * the key exists, but deleted, update it and return true.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean insertUserKey(UserKey userKey) {
        boolean res = false;

        try {
            mapper.insertUserKey(userKey);
            res = true;
        } catch (DuplicateKeyException e) {
            // key exists
            UserKey key =
                mapper.selectUserKey(userKey.getUserId(), userKey.getApplicationId(), userKey.getOauthClientId());

            if (key != null) {
                if (key.isDeleted()) {
                    mapper.updateUserKey(userKey);
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateUserKey(UserKey userKey) {
        mapper.updateUserKey(userKey);
    }

    @Override
    // @CacheEvict(cacheNames=CACHE_USERS_BY_KEY)
    public boolean deleteUserKey(String userKey) {
        return mapper.deleteUserKey(userKey) > 0;
    }

    @Override
    public boolean deleteAllUserKeys(int userId) {
        return mapper.deleteAllUserKeys(userId) > 0;
    }

    @Override
    public void updateUserAvatar(int userId, String imgName) {
        mapper.updateUserAvatar(userId, imgName);
    }

    @Override
    public void updateUserPassword(int userId, String password) {
        mapper.updateUserPassword(userId, password);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean insertUserBasicInfo(User user) {
        mapper.insertUserBasicInfo(user);
        int userId = user.getUserId();
        if (!CommonUtil.isNullOrEmpty(user.getProperites())) {
            this.insertUserProperties(user.getUserId(), user.getProperites());
        }
        return userId > 0;
    }

    @Override
    public boolean checkUserSamePhoneNumber(String phoneNumber) {
        return mapper.checkUserSamePhoneNumber(phoneNumber) > 0;
    }

    @Override
    public User selectUserByPhoneNumber(String phoneNumber) {
        return mapper.selectUserByPhoneNumber(phoneNumber);
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return mapper.selectUserByPhoneNumber(phoneNumber);
    }

    @Override
    public void updateUserVerified(int userId) {
        mapper.updateUserVerified(userId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        mapper.updateUser(user);
        if (!CommonUtil.isNullOrEmpty(user.getProperites())) {
            this.insertUserProperties(user.getUserId(), user.getProperites());
        }
    }

    @Override
    public boolean checkUserSameNickName(String nickname) {
        return mapper.checkUserSameNickname(nickname) > 0;
    }

    @Override
    public List<User> getUsers(List<Integer> userIds, Integer roleId, Boolean detail) {
        return mapper.selectUsers(userIds, roleId, detail);
    }

    @Override
    public List<User> getApplyUsers(Integer targetUserId, Timestamp start, Timestamp end, Boolean detail) {
        return mapper.selectApplyUsers(targetUserId, start, end, detail, null);
    }

    @Override
    public List<User> getApplyUsersByUserId(int userId, Boolean detail) {
        return mapper.selectApplyUsers(null, null, null, detail, userId);
    }

    @Override
    public void saveUserApplyRecord(int userId, int targetUserId) {
        mapper.insertUserApplyRecord(userId, targetUserId, DateUtil.getCurrentTimeStamp());

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUserApplyRecords(int userId, int[] targetUserIds) {
        if (CommonUtil.isNullOrEmpty(targetUserIds)) {
            return;
        }
        for (int i = 0; i < targetUserIds.length; i++) {
            saveUserApplyRecord(userId, targetUserIds[i]);
        }
    }

    @Override
    public boolean checkUserApplied(int userId, Timestamp start) {
        return mapper.checkUserApplied(userId, start) > 0;
    }

    @Override
    public void updateTargetUserReason(int userId, int targetUserId, String reason, String reasonDesc, Integer point) {
        mapper.updateTargetUserReason(userId, targetUserId, reason, reasonDesc, point);
    }

    @Override
    public boolean checkUserIdExist(int userId) {
         return mapper.checkUserIdExist(userId) > 0;
    }

    
    
    
}
