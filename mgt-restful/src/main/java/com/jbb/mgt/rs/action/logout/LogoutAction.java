package com.jbb.mgt.rs.action.logout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.domain.Account;
import com.jbb.mgt.server.rs.action.BasicAction;

@Service(LogoutAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LogoutAction extends BasicAction {
    public static final String ACTION_NAME = "LogoutAction";

    private static Logger logger = LoggerFactory.getLogger(LogoutAction.class);

    public void logout(String userKey) {
        logger.debug(">logoutUser()");

        Account account = coreAccountService.logoutAccount(userKey);

        if (account != null) {
            setResultCode(SUCCESS, null);

        } else {
            logger.debug("Wrong API key: [{}]", userKey);
            escalateErrorCode(WRONG_API_KEY);
        }

        logger.debug("<logoutUser()");
    }

}
