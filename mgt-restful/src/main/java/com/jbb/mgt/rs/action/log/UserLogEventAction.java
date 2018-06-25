package com.jbb.mgt.rs.action.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.service.UserEventLogService;
import com.jbb.mgt.rs.action.login.LoginAction;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.server.common.util.DateUtil;

@Service(UserLogEventAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserLogEventAction extends BasicAction {

    public static final String ACTION_NAME = "UserLogEventAction";

    private static Logger logger = LoggerFactory.getLogger(LoginAction.class);

    @Autowired
    UserEventLogService userEventLogService;

    public void createLogEvent(Integer userId, String cookieId, String sourceId, String eventName, String eventLabel,
        String eventAction, String eventValue) {
        logger.debug(">createLogEvent()");

        userEventLogService.insertEventLog(userId, sourceId, cookieId, eventName, eventAction, eventLabel, eventValue,
            this.getRemoteAddress(), DateUtil.getCurrentTimeStamp());
        logger.debug("<createLogEvent()");
    }
}
