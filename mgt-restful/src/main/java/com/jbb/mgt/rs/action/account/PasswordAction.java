
package com.jbb.mgt.rs.action.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbb.mgt.server.rs.action.BasicAction;

@Service(PasswordAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PasswordAction extends BasicAction {
    public static final String ACTION_NAME = "PasswordAction";

    private static Logger logger = LoggerFactory.getLogger(PasswordAction.class);

    public void resetPassword(String newPassword) {
        logger.debug(">resetPassword() ");
        this.coreAccountService.resetPassword(account.getAccountId(), newPassword);
        logger.debug("<resetPassword");
    }

}
