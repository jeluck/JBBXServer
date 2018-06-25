package com.jbb.server.rs.action;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.common.Constants;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserKey;
import com.jbb.server.core.domain.UserLoginLog;
import com.jbb.server.core.service.UserLoginLogsService;
import com.jbb.server.rs.pojo.ActionResponse;

/**
 * @Type LoginAction.java
 * @Desc
 * @author VincentTang
 * @date 2017年10月30日 下午6:08:16
 * @version
 */
@Service(LoginAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LoginAction extends BasicAction {
    private static Logger logger = LoggerFactory.getLogger(LoginAction.class);
    
    private static final int[] LOGIN_BY_KEY_APPS = {
        UserKey.APPLICATION_USER, UserKey.APPLICATION_TEMP_USER_KEY
    };

    public static final String ACTION_NAME = "loginAction";

    private Response response;

    @Autowired
    UserLoginLogsService userLoginLogsService;
    
    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    public User loginByKey(String userKey, Integer daysToExpire, Integer keyType) {
        if (logger.isDebugEnabled()) {
            logger.debug(">loginByKey() daysToExpire=" + daysToExpire + ", keyType=" + keyType);
        }

        User user = coreAccountService.login(userKey, LOGIN_BY_KEY_APPS);

        int keyAppId = keyType != null ? keyType : UserKey.APPLICATION_USER;
        long expiry;

        if (UserKey.APPLICATION_USER == keyAppId) {
            expiry = daysToExpire == null ? -1 : daysToExpire * Constants.ONE_DAY_MILLIS;
        } else if (UserKey.APPLICATION_TEMP_USER_KEY == keyAppId) {
            expiry = PropertyManager.getLongProperty("jbb.login.temp.key.expiry", 900L) * 1000L;
        } else {
            throw new WrongParameterValueException("Unsupported key type");
        }

        createData(user, expiry, keyAppId);

        logger.debug("<loginByUserKey()");
        return user;
    }
    
    

    public User loginByPassword(String phoneNumber, String password, Integer daysToExpire, String platform, Integer keyType) {
        logger.debug(">login() phoneNumber={}, daysToExpire={}", phoneNumber, daysToExpire);
        int keyAppId = keyType != null ? keyType : UserKey.APPLICATION_USER;
        User user = coreAccountService.loginByPassword(phoneNumber, password, platform);
        userLoginLogsService.saveUserLoginLogs(new UserLoginLog(user.getUserId(), platform , this.getRemoteAddress(), DateUtil.getCurrentTimeStamp()));
        createData(user, daysToExpire == null ? -1 : daysToExpire * Constants.ONE_DAY_MILLIS, keyAppId);
        
        logger.debug("<login()");
        return user;
    }

    public User loginByMsgCode(String phoneNumber, String msgCode, Integer daysToExpire, String platform, Integer keyType) {
        logger.debug(">login() phoneNumber={}, daysToExpire={}", phoneNumber, daysToExpire);
        int keyAppId = keyType != null ? keyType : UserKey.APPLICATION_USER;
        User user = coreAccountService.loginByCode(phoneNumber, msgCode, platform);
        createData(user, daysToExpire == null ? -1 : daysToExpire * Constants.ONE_DAY_MILLIS, keyAppId);
        
        logger.debug("<login()");
        return user;
    }

    private void createData(User user, long expiry, int keyAppId) {
        int userId = user.getUserId();

        UserKey userKey = user.getUserKey();
        boolean createNewKey = (userKey == null) || (userKey.getApplicationId() != keyAppId);

        if (!createNewKey) {
            Timestamp keyExpiry = userKey.getExpiry();
            createNewKey =
                (keyExpiry == null) || (keyExpiry.getTime() < System.currentTimeMillis() + Constants.ONE_DAY_MILLIS);
        }

        if (createNewKey) {
            // create new user key
            userKey = coreAccountService.createUserKey(userId, keyAppId, expiry, true);
        }

        response.key = userKey.getUserKey();
        Timestamp keyExpiry = userKey.getExpiry();
        response.keyExpire = StringUtil.printDateTime(keyExpiry, DateUtil.getCurrentCalendar());
        response.keyExpireMs = keyExpiry.getTime();

    }
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private String key;
        private String keyExpire;
        private long keyExpireMs;

        public String getKey() {
            return key;
        }

        public String getKeyExpire() {
            return keyExpire;
        }

        public long getKeyExpireMs() {
            return keyExpireMs;
        }
    }

}