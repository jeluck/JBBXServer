package com.jbb.mgt.rs.action.integrate;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.core.service.UserService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;

@Component(UserListAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserListAction extends BasicAction {

    public static final String ACTION_NAME = "UserListAction";

    private static Logger logger = LoggerFactory.getLogger(UserListAction.class);

    private Response response;

    @Autowired
    @Qualifier("UserService")
    private UserService userService;

    @Override
    protected ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    public void getUserByPhoneNumber(String phoneNumber) {
        logger.debug(">getUserByPhoneNumber()");
        response.list = userService.selectUserByPhoneNumber(phoneNumber);
        logger.debug("<getUserByPhoneNumber()");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private List<User> list;

        public List<User> getList() {
            return list;
        }

    }

}
