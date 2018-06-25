package com.jbb.mgt.rs.action.integrate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.UserContant;
import com.jbb.mgt.core.service.UserContantService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.server.common.exception.MissingParameterException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(UserContantAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserContantAction extends BasicAction {

    public static final String ACTION_NAME = "UserContantAction";

    private static Logger logger = LoggerFactory.getLogger(UserContantAction.class);

    @Autowired
    private UserContantService userContantService;

    public void insertContants(Request req,Integer userId) {
        logger.debug(">insertContants, userContants={}", req);
        if(CollectionUtils.isEmpty(req.userContants)){
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "userContants");
        }
        if(userId == null || userId == 0){
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "userId");
        }
        userContantService.jbbToMgtInsertOrUpdateUserContant(req.userContants,userId);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Request {
        public List<UserContant> userContants;
    }

}
