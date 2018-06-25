package com.jbb.mgt.core.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbb.mgt.core.dao.UserDao;
import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.core.domain.UserKey;
import com.jbb.mgt.core.service.UserService;
import com.jbb.server.common.Constants;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.ExecutionException;
import com.jbb.server.common.exception.WrongUserKeyException;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;

@Service("UserService")
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final int ACCOUNT_KEY_SIZE = 64;

    @Autowired
    private UserDao userDao;

    @Override
    public void insertUser(User user) {
        userDao.insertUser(user);

    }

    @Override
    public User selectUserById(int userId) {
        return userDao.selectUserById(userId);
    }

    @Override
    public User selectJbbUserById(int jbbUserId, int orgId) {
        return userDao.selectJbbUserById(jbbUserId, orgId);
    }

    @Override
    public List<User> selectUsers(String phoneNumber, String passWord, String channelCode, Timestamp startDate,
        Timestamp endDate) {
        return userDao.selectUsers(channelCode, startDate, endDate);
    }

    @Override
    public int countUserByChannelCode(String channelCode, Timestamp startDate, Timestamp endDate) {
        return userDao.countUserByChannelCode(channelCode, startDate, endDate);
    }

    @Override
    public User selectUser(int orgId, String phoneNumber) {
        return userDao.selectUser(orgId, phoneNumber);
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User login(String userKey) throws WrongUserKeyException {
        if (userKey == null)
            throw new WrongUserKeyException();

        User user = userDao.selectUserByUserKey(userKey);

        if (user == null) {
            throw new WrongUserKeyException();
        }

        /* loginProcess(user);*/
        return user;
    }

    /* private void loginProcess(User user) {
        // 获取用户权限
        user.setPermissions(userDao.getAccountPermissions(account.getAccountId()));
    }*/

    @Override
    public UserKey createUserKey(int userId, int applicationId, long expiry, boolean extend) {
        return createUserKey(userId, applicationId, expiry, ACCOUNT_KEY_SIZE, extend);
    }

    @Override
    public UserKey selectUserKeyByUserIdAndAppId(int userId, int applicationId) {
        return userDao.selectUserKeyByUserIdAndAppId(userId, applicationId);
    }

    @Override
    public void insertUserKey(UserKey userKey) {
        userDao.insertUserKey(userKey);
    }

    @Override
    public void updateUserKey(UserKey userKey) {
        userDao.updateUserKey(userKey);
    }

    /**
     * Generate a new user key for authenticated user
     *
     * @param userId user ID
     * @param expiry key expiry period in milliseconds (-1 means it will not be expired)
     * @param keySize new key size in bytes
     * @param extend extend key expiry date, if it is less than requested date
     * @return user key
     */
    private UserKey createUserKey(int userId, int applicationId, long expiry, int keySize, boolean extend) {
        if (logger.isDebugEnabled()) {
            logger.debug("|>createUserKey() userId=" + userId + ", expiry=" + expiry + ", keySize=" + keySize
                + ", extend=" + extend);
        }

        long maxKeyExpiry = PropertyManager.getIntProperty("jbb.core.userKey.maxExpiry", -1) * Constants.ONE_DAY_MILLIS;
        if ((maxKeyExpiry > 0) && ((expiry <= 0) || (expiry > maxKeyExpiry))) {
            expiry = maxKeyExpiry;
        }

        long currentTime = DateUtil.getCurrentTime();
        long expiryTime = expiry <= 0 ? Constants.LAST_SYSTEM_MILLIS : currentTime + expiry;

        boolean success = false;
        Exception ex = null;
        UserKey userKey = null;

        for (int i = 0; !success && (i < 10); i++) {
            boolean createNew = true;
            boolean update = false;

            userKey = userDao.selectUserKeyByUserIdAndAppId(userId, applicationId);

            if (userKey != null) {
                update = true;

                long keyExpiry = userKey.getExpiry().getTime();
                if (!userKey.isDeleted() && (keyExpiry > currentTime)) {
                    if (extend) {
                        createNew = false;
                        extend = (keyExpiry < expiryTime - Constants.ONE_DAY_MILLIS)
                            || (expiryTime < currentTime + Constants.ONE_DAY_MILLIS) && (keyExpiry < expiryTime);
                    } else if (keyExpiry > currentTime + Constants.ONE_DAY_MILLIS) {
                        createNew = false;
                    }
                }
            }

            if (createNew) {
                userKey = generateUserKey(expiryTime, keySize);
                userKey.setUserId(userId);
                userKey.setApplicationId(applicationId);
                try {
                    if (update) {
                        userDao.updateUserKey(userKey);
                    } else if (!userDao.insertUserKey(userKey)) {
                        long keyExpiry = userKey.getExpiry().getTime();
                        if (keyExpiry <= currentTime) {
                            // current key expired
                            continue;
                        }
                    }
                } catch (DeadlockLoserDataAccessException e) {
                    ex = e;
                    if (logger.isDebugEnabled()) {
                        logger.debug("Deadlock in " + (update ? "updating" : "inserting") + " new user key " + userKey
                            + ", extend=" + extend + " : " + e.toString());
                    }
                    continue;
                } catch (Exception e) {
                    ex = e;
                    logger.warn("Exception in " + (update ? "updating" : "inserting") + " new user key " + userKey
                        + ", extend=" + extend, e);
                    continue;
                }
            } else if (extend) {
                // extend expiry time
                userKey.setExpiry(new Timestamp(expiryTime));
                try {
                    userDao.updateUserKey(userKey);
                } catch (DeadlockLoserDataAccessException e) {
                    ex = e;
                    if (logger.isDebugEnabled()) {
                        logger.debug("Deadlock in extending new user key " + userKey + " : " + e.toString());
                    }
                    continue;
                } catch (Exception e) {
                    ex = e;
                    logger.warn("Exception in extending new user key " + userKey, e);
                    continue;
                }
            }

            success = true;
        }

        if (!success) {
            throw new ExecutionException("Cannot generate user key for userId=" + userId + ", keySize=" + keySize, ex);
        }

        logger.debug("|<createUserKey()");
        return userKey;
    }

    private UserKey generateUserKey(long expiryTime, int keySize) {
        UserKey userKey = new UserKey();
        userKey.setUserKey(StringUtil.secureRandom(keySize).substring(0, keySize));
        userKey.setExpiry(new Timestamp(expiryTime));

        return userKey;
    }

    @Override
    public User logoutUser(String userKey) {
        logger.debug(">logoutUser()");
        User user = userDao.selectUserByUserKey(userKey);
        if (user == null) {
            logger.debug("<logoutUser() not authenticated");
            return null;
        }
        userDao.deleteUserKey(userKey);
        logger.debug("<logoutUser()");
        return user;
    }

    @Override
    public Integer getPushCount(Integer orgId, Integer type) {
        return userDao.getPushCount(orgId, type);
    }

    @Override
    public User selectUserByApplyId(int applyId) {
        return userDao.selectUserByApplyId(applyId);
    }

    @Override
    public List<User> selectUserDetails(String channelCode, Timestamp startDate, Timestamp endDate, Integer orgId,
        Boolean isGetJbbChannels) {
        return userDao.selectUserDetails(channelCode, startDate, endDate, orgId, isGetJbbChannels);
    }

    @Override
    public List<User> selectUserByPhoneNumber(String phoneNumber) {
        return userDao.selectUserByPhoneNumber(phoneNumber);
    }

}
