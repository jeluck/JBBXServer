package com.jbb.mgt.core.dao;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.core.domain.UserKey;

public interface UserDao {

    // 新增用户
    void insertUser(User user);

    // 通过ID获取用户
    User selectUserById(int userId);

    // 通过jbbUserId查询用户
    User selectJbbUserById(int jbbUserId, int orgId);

    // 通过借款申请ID获取用户信息
    User selectUserByApplyId(int applyId);

    /**
     * 按渠道获取用户
     * 
     * @param channelCode 渠道编号
     * @param startDate 开始时间
     * @param startDate 结束时间
     * @return
     */

    List<User> selectUsers(String channelCode, Timestamp startDate, Timestamp endDate);

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
    List<User> selectUserDetails(String channelCode, Timestamp startDate, Timestamp endDate, Integer orgId,
        Boolean isGetJbbChannels);

    /**
     * 查询统计数
     *
     * @param channelCode
     * @param startDate
     * @param endDate
     * @return
     */
    int countUserByChannelCode(String channelCode, Timestamp startDate, Timestamp endDate);

    /**
     * 按渠道与手机号获取用户
     *
     * @param orgId
     * @param phoneNumber
     * @return
     */
    User selectUser(int orgId, String phoneNumber);

    // 修改用户信息
    void updateUser(User user);

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
    void deleteUserKeyByUserId(int userId);

    /**
     * 删除所有符合的用户key
     * 
     * @param userKey 用户key
     * @return
     */
    boolean deleteUserKey(String userKey);

    /**
     * 查询用户key信息
     * 
     * @param userId 用户id
     * @param applicationId 地址id
     * @return
     */
    UserKey selectUserKeyByUserIdAndAppId(int userId, int applicationId);

    /**
     * 查询 user信息
     * 
     * @param userKey 用户key
     * @return
     */
    User selectUserByUserKey(String userKey);

    /**
     * 修改用户key
     * 
     * @param userKey 用户key对象
     * @return
     */
    void updateUserKey(UserKey userKey);

    Integer getPushCount(Integer orgId, Integer type);

    /**
     * 通过手机号获取用户列表
     * 
     * @param phoneNumber 电话号码
     * @return
     */
    List<User> selectUserByPhoneNumber(String phoneNumber);

    /**
     * 检查用户是否在start时间后推荐过出借组织 
     * @param userId
     * @param start
     * @return
     */
    boolean checkUserApplied(int userId, Timestamp start);
    
    boolean checkUserExistInOrg(int orgId, String phoneNumber, Integer jbbUserId, String idCard);
}
