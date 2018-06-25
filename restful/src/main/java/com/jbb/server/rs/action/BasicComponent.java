package com.jbb.server.rs.action;

import java.util.Calendar;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jbb.server.common.exception.AccessDeniedException;
import com.jbb.server.common.exception.WrongUserKeyException;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.HttpUtil;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserKey;
import com.jbb.server.core.service.AccountService;
import com.jbb.server.rs.pojo.ActionResponse;
import com.jbb.server.shared.rs.InformationCodes;

public abstract class BasicComponent implements InformationCodes {
    private static Logger logger = LoggerFactory.getLogger(BasicComponent.class);
    private static final int[] USER_KEY_APPS = {UserKey.APPLICATION_USER};
    private static final int[] ADMIN_KEY_APPS = {UserKey.APPLICATION_USER};
    
    private static final String MGT_PLATFORM_API_KEY = "xqptZa4WaKFRes9xMlA_G8PSi5eAeSon-z9Ol7ipefXdcSD_tWi6raaIdrmHJDas";

    private ActionResponse actionResponse = makeActionResponse();
    /** End user. !!! Do not modify this user object. It is in cache memory. */
    protected User user;

    /** Admin user. !!! Do not modify this user object. It is in cache memory. */
    protected User adminUser;

    private HttpServletRequest httpRequest;
    private TimeZone requestTimezone;
    private Calendar requestCalendar;

    @Autowired
    protected AccountService coreAccountService;

    public void setContextInstances(HttpServletRequest request) {
        if (request != null) {
            this.httpRequest = request;
            this.setTimezone(request.getParameter("timeZone"));
        }
    }
    
    public void validateMgtPlatformApiKey(String apiKey) throws WrongUserKeyException {
       if(!MGT_PLATFORM_API_KEY.equals(apiKey)){
           throw new WrongUserKeyException();
       }
    }


    public void validateUserKey(String userKey) throws WrongUserKeyException {
        authenticate(userKey);
    }

    private void authenticate(String userKey) throws WrongUserKeyException {
        user = coreAccountService.login(userKey, USER_KEY_APPS);
        if (user == null) {
            logger.error("Not found user, API_KEY = " + userKey);
            throw new WrongUserKeyException();
        }

        logger.debug("Authenticated user: {}", user);
    }

    public void validateSystemAdminPermission() {
        if (user.checkAccessAllPermission()) {
            adminUser = user;
        } else {
            throw new AccessDeniedException("jbb.error.validateAdminAccess.accessDenied", "zh");
        }
    }

    public void validateUserAccess() {
        int adminUserId = user.getUserId();

        if (user.canGetRecommandUsers() || user.checkAccessAllPermission()) {
            adminUser = user;
        } else {
            throw new AccessDeniedException("jbb.error.validateAdminAccess.accessDenied", "zh");
        }

    }

    public void validateStaticticAccess() {
        int adminUserId = user.getUserId();
        if (user.canStatisticUsers() || user.checkAccessAllPermission()) {
            adminUser = user;
        } else {
            throw new AccessDeniedException("jbb.error.validateAdminAccess.accessDenied", "zh");
        }

    }

    protected String getRemoteAddress() {
        return httpRequest != null ? HttpUtil.getClientIpAddress(httpRequest) : null;
    }

    protected ActionResponse makeActionResponse() {
        return null;
    }

    public void escalateErrorCode(int forceErrorCode) {
        actionResponse.escalateErrorCode(forceErrorCode);
    }

    public void escalateErrorCode() {
        actionResponse.escalateErrorCode(FAILURE);
    }

    public void setResultCode(int resultCode, String resultCodeMessage) {
        actionResponse.setResultCodeAndMessage(resultCode, resultCodeMessage);
    }

    public final ActionResponse getActionResponse() {
        return actionResponse;
    }

    public TimeZone getTimezone() {
        if (requestTimezone != null)
            return requestTimezone;
        return TimeZone.getDefault();
    }

    public void setTimezone(String timezone) {
        if (timezone != null) {
            this.requestTimezone = TimeZone.getTimeZone(timezone);
            this.requestCalendar = DateUtil.getCurrentCalendar(requestTimezone);
        }
    }
    

}
