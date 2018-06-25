package com.jbb.server.core.dao.mapper;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.server.core.domain.UserProperty;
import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.Role;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserKey;

public interface AccountMapper {
    
    Role selectRoleById(int roleId);
    List<Integer> selectRolePermissions(int roleId);
    List<Role> selectAllRoles();

    /**
     * 获取用户的属性信息
     * @param userId
     * @param name 前缀匹配
     * @return
     */
    List<UserProperty> selectUserProperties(@Param("userId") int userId, @Param("name") String name);

    /**
     * 通过userId和属性键查到用户属性信息
     * @param userId
     * @param name
     * @return
     */
    UserProperty selectUserPropertyByUserIdAndName(@Param("userId") int userId,@Param("name") String name);

    /**
     * 通过userId和属性键删除用户属性信息
     * @param userId
     * @param name
     * @return int
     */
    int deleteUserPropertyByUserIdAndName(@Param("userId") int userId,@Param("name") String name);

    /**
     * 修改用户属性信息
     * @param userId
     * @param name
     * @return
     */
    int updateUserPropertyByUserIdAndName(@Param("userId") int userId,@Param("name") String name,@Param("value")String value);

    /**
     * 增加用户属性信息
     * @param userId
     * @param name
     * @param value
     * @return
     */
    int insertUserProperty(@Param("userId") int userId,@Param("name") String name,@Param("value")String value);

    /**
     * 获取用户信息
     * 
     * @param userId
     * @return
     */
    User selectUserByUserId(@Param("userId") int userId);

    /**
     * 获取用户信息
     * 
     * @param phoneNumber
     * @return
     */
    User selectUserByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    User selectUserByKey(@Param("userKey") String userKey, @Param("expiry") Timestamp expiry);

    UserKey selectUserKey(@Param("userId") int userId, @Param("applicationId") int applicationId,
        @Param("oauthClientId") String oauthClientId);

    void insertUserKey(UserKey userKey);

    void updateUserKey(UserKey userKey);

    int deleteUserKey(String userKey);

    int deleteAllUserKeys(int userId);

    void updateUserAvatar(@Param("userId") int userId, @Param("imgName") String imgName);

    void updateUserPassword(@Param("userId") int userId, @Param("password") String password);

    int insertUserBasicInfo(User user);

    int checkUserSamePhoneNumber(@Param("phoneNumber") String phoneNumber);
    
    void updateUserVerified(@Param("userId") int userId);
    
    void updateUser(User user);
    
    int checkUserSameNickname(@Param("nickname") String nickname);

    List<User> searchIntentionalLenders(String iouCode);

    List<User> selectUsers(@Param("userIds") List<Integer> userIds, @Param("roleId") Integer roleId, @Param("detail") Boolean detail );
    
    List<User> selectApplyUsers(@Param("targetUserId") Integer targetUserId, @Param("start") Timestamp start, @Param("end") Timestamp end, @Param("detail") Boolean detail, @Param("userId") Integer userId);
    
    void insertUserApplyRecord(@Param("userId") int userId, @Param("targetUserId") int targetUserId, @Param("creationDate") Timestamp creationDate);
   
    int checkUserApplied(@Param("userId") int userId, @Param("start") Timestamp start);
    
    int checkUserIdExist(@Param("userId") int userId);

    void updateTargetUserReason(@Param("userId") int userId, @Param("targetUserId") int targetUserId, @Param("reason") String reason, @Param("reasonDesc") String reasonDesc, @Param("point") Integer point );

}
