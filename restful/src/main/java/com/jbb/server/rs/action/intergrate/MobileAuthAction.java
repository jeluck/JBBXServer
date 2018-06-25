package com.jbb.server.rs.action.intergrate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbb.server.common.Constants;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.exception.ObjectNotFoundException;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.domain.User;
import com.jbb.server.rs.action.AliyunPolicyAction;
import com.jbb.server.rs.action.BasicAction;

@Service(MobileAuthAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MobileAuthAction extends BasicAction {
    private static Logger logger = LoggerFactory.getLogger(MobileAuthAction.class);
    public static final String ACTION_NAME = "MobileAuthAction";

    public void authMobile(String phoneNumber, int userId) {
        logger.debug(">authMobile, phoneNumber = " + phoneNumber + ", userId="+userId);

        if (StringUtil.isEmpty(phoneNumber)) {
            throw new MissingParameterException("jbb.error.exception.missing.param", "zh", "phoneNumber");
        }

        User user = this.coreAccountService.getUserByPhoneNumber(phoneNumber);

        if (user == null) {
            throw new ObjectNotFoundException("jbb.error.exception.phoneNotFound");
        }

        if (user.getUserId() != userId) {
            throw new ObjectNotFoundException("jbb.error.exception.userPhoneNumberError");
        }

        this.coreAccountService.saveUserVerifyResult(user.getUserId(), Constants.VERIFY_TYPE_MOBILE, 1, true);

        logger.debug("<authMobile");

    }

}
