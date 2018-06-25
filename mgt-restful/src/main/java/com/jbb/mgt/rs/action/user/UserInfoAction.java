package com.jbb.mgt.rs.action.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;

@Service(UserInfoAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserInfoAction extends BasicAction {

    public static final String ACTION_NAME = "UserInfoAction";

    private static Logger logger = LoggerFactory.getLogger(UserInfoAction.class);

    private Response response;

    @Override
    protected ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    public void selectUserInfo() {
        logger.debug(">selectUserInfo()");
        User userInfo = this.user;
        userInfo.setKey(null);
        response.user = userInfo;
        logger.debug(">selectUserInfo()");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private User user;

        public User getUser() {
            return user;
        }

    }

}
