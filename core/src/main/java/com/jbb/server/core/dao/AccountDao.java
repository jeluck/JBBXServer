package com.jbb.server.core.dao;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.server.core.domain.Role;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserKey;
import com.jbb.server.core.domain.UserProperty;

public interface AccountDao {
    /**
     * 通过userId和属性键查到用户属性信息
     * 
     * @param userId
     * @param name
     * @return
     */
    UserProperty selectUserPropertyByUserIdAndName(int userId, String name);

    /**
     * 通过userId和属性键删除用户属性信息
     * 
     * @param userId
     * @param name
     * @return int
     */
    boolean deleteUserPropertyByUserIdAndName(int userId, String name);

    /**
     * 修改用户属性信息
     * 
     * @param userId
     * @param name
     * @return
     */
    boolean updateUserPropertyByUserIdAndName(int userId, String name, String value);

    /**
     * 增加用户属性信息
     * 
     * @param userId
     * @param name
     * @param value
     * @return
     */
    boolean insertUserProperty(int userId, String name, String value);
    
    
    void insertUserProperties(int userId, List<UserProperty> properties);

    User getUser(int userId);

    User getUserByPhoneNumber(String phoneNumber);

    /**
     * Login using user key
     * 
     * @return User
     */
    User authenticate(String key);

    List<User> getUsers(List<Integer> userIds, Integer roleId, Boolean detail);
    /**
     * Select user key
     * 
     * @return user key or null
     */
    UserKey getUserKey(int userId, int applicationId, String oauthClientId);

    /**
     * Update existing user key by user ID
     */
    void updateUserKey(UserKey userKey);

    /**
     * Insert new user key
     */
    boolean insertUserKey(UserKey userKey);

    /**
     * Delete existing user key
     * 
     * @return true if the key is deleted
     */
    boolean deleteUserKey(String userKey);

    boolean deleteAllUserKeys(int userId);

    void updateUserAvatar(int userId, String imgName);

    void updateUserPassword(int userId, String password);

    boolean insertUserBasicInfo(User user);

    boolean checkUserSamePhoneNumber(String phoneNumber);
    
    boolean checkUserIdExist(int userId);

    User selectUserByPhoneNumber(String phoneNumber);

    void updateUser(User user);

    void updateUserVerified(int userId);

    boolean checkUserSameNickName(String nickname);

    Role getRole(int roleId);

    int[] getRolePermissions(int roleId);

    Role[] getAllRoles();
    
    List<User> getApplyUsers(Integer userId, Timestamp start, Timestamp end, Boolean detail);
    
    void saveUserApplyRecord( int userId,  int targetUserId);

    void saveUserApplyRecords( int userId,  int[] targetUserIds);

    List<UserProperty> selectUserProperties(int userId, String name);

    List<User> getApplyUsersByUserId(int userId, Boolean detail);
    
    boolean checkUserApplied(int userId, Timestamp start);
    
    void updateTargetUserReason(int userId,  int targetUserId, String reason, String reasonDesc, Integer point );

    
}
