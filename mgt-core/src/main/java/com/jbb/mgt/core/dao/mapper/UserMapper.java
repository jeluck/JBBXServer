package com.jbb.mgt.core.dao.mapper;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.core.domain.UserKey;

/**
 * 
 * @author hsy
 * 
 *
 */
public interface UserMapper {

    void insertUser(User user);

    User selectUserById(@Param(value = "userId") int userId);

    User selectUserByJbbUserId(@Param(value = "jbbUserId") int userId, @Param(value = "orgId") int orgId);

    User selectUserByApplyId(@Param(value = "applyId") int applyId);

    User selectUser(@Param("orgId") int orgId, @Param("phoneNumber") String phoneNumber);

    void updateUser(User user);

    /**
     * 获取渠道数据列表
     * 
     * @param channelCode 渠道编号
     * @param startDate 开始时间
     * @param startDate 结束时间
     * @return
     */
    List<User> selectUsers(@Param("channelCode") String channelCode, @Param("startDate") Timestamp startDate,
        @Param("endDate") Timestamp endDate);

    /**
     * 按渠道获取用户明细
     * 
     * @param startDate 开始时间
     * @param startDate 结束时间
     * @param channelCode
     * @param orgId
     * @param isGetJbbChannels 是否获取帮帮导流的明细。默认为false. false为不获取， true为仅获取帮帮导流的用户明细
     * @return
     */
    List<User> selectUserDetails(@Param("channelCode") String channelCode, @Param("startDate") Timestamp startDate,
        @Param("endDate") Timestamp endDate, @Param("orgId") Integer orgId,
        @Param("isGetJbbChannels") Boolean isGetJbbChannels);

    /**
     * 查询统计数
     *
     * @param channelCode
     * @param startDate
     * @param endDate
     * @return
     */
    int countUserByChannelCode(@Param("channelCode") String channelCode, @Param("startDate") Timestamp startDate,
        @Param("endDate") Timestamp endDate);

    /**
     * 新增用户key
     * 
     * @param userKey 用户key对象
     * @return
     */
    boolean insertUserKey(UserKey userKey);

    /**
     * 删除所有符合的用户key
     * 
     * @param userId 用户id
     * @return
     */
    void deleteUserKeyByUserId(@Param(value = "userId") int userId);

    /**
     * 删除所有符合的用户key
     * 
     * @param userKey 用户key
     * @return
     */
    int deleteUserKey(@Param(value = "userKey") String userKey);

    /**
     * 查询用户key信息
     * 
     * @param userId 用户id
     * @param applicationId 地址id
     * @return
     */
    UserKey selectUserKeyByUserIdAndAppId(@Param(value = "userId") int userId,
        @Param(value = "applicationId") int applicationId);

    /**
     * 查询 user信息
     * 
     * @param userKey 用户key
     * @return
     */
    User selectUserByUserKey(@Param(value = "userKey") String userKey, @Param("expiry") Timestamp expiry);

    /**
     * 修改用户key
     * 
     * @param userKey 用户key对象
     * @return
     */
    void updateUserKey(UserKey userKey);

    Integer getPushCount(@Param(value = "orgId") Integer orgId, @Param(value = "type") Integer type);

    /**
     * 通过手机号获取用户列表
     * 
     * @param phoneNumber 电话号码
     * @return
     */
    List<User> selectUserByPhoneNumber(String phoneNumber);

    /**
     * 检查用户是否
     * 
     * @param userId
     * @param start
     * @return
     */
    Integer checkUserApplied(@Param(value = "userId") int userId, @Param(value = "start") Timestamp start);

    Integer checkUserExistInOrg(@Param(value = "orgId") int orgId, @Param(value = "phoneNumber") String phoneNumber,
        @Param(value = "jbbUserId") Integer jbbUserId, @Param(value = "idCard") String idCard);
}
