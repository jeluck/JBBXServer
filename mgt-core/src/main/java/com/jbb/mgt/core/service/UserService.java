package com.jbb.mgt.core.service;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.core.domain.UserKey;
import com.jbb.server.common.exception.WrongUserKeyException;

public interface UserService {

    /**
     * 新增用户
     *
     * @param user
     */
    void insertUser(User user);

    /**
     * 通过ID获取用户
     *
     * @param userId
     * @return
     */
    User selectUserById(int userId);

    /**
     * 通过jbbUserID获取用户
     *
     * @param jbbUserId
     * @param orgId
     * @return
     */
    User selectJbbUserById(int jbbUserId, int orgId);

    User selectUserByApplyId(int applyId);

    /**
     * 按组织与手机号获取用户
     *
     * @param orgId
     * @param phoneNumber
     * @return
     */
    User selectUser(int orgId, String phoneNumber);

    // 更新用户
    void updateUser(User user);

    // 账户通过userKey登录
    User login(String userKey) throws WrongUserKeyException;

    UserKey selectUserKeyByUserIdAndAppId(int userId, int applicationId);

    void insertUserKey(UserKey userKey);

    void updateUserKey(UserKey userKey);

    UserKey createUserKey(int userId, int applicationId, long expiry, boolean b);

    User logoutUser(String userKey);

    /**
     * 查询用户列表
     * 
     * @param passWord 密码
     * @param phoneNumber 账户
     * @param channelCode 渠道号
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    List<User> selectUsers(String phoneNumber, String passWord, String channelCode, Timestamp startDate,
        Timestamp endDate);

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

    Integer getPushCount(Integer orgId, Integer type);

    /**
     * 通过手机号获取用户列表
     * 
     * @param phoneNumber 电话号码
     * @return
     */
    List<User> selectUserByPhoneNumber(String phoneNumber);
}
