package com.jbb.mgt.rs.action.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbb.mgt.core.domain.AliPolicy;
import com.jbb.mgt.core.service.impl.AliyunServiceImpl;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.common.util.StringUtil;

@Component(UserPolicyAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserPolicyAction extends BasicAction {
    private static Logger logger = LoggerFactory.getLogger(UserPolicyAction.class);
    public static final String ACTION_NAME = "UserPolicyAction";

    private Response response;

    @Autowired
    private AliyunServiceImpl aliyunServiceImpl;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    public void getPolicy(String filePrifix) {
        if(StringUtil.isEmpty(filePrifix)){
            filePrifix = AliyunServiceImpl.PHOTO_FILE_PREFIX;
        }
        
      // this.response.policy = aliyunServiceImpl.getPostPolicy(user.getUserId(), filePrifix);
        logger.debug("<createUserInfo()");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {

        private AliPolicy policy;

        @JsonProperty("policy")
        public AliPolicy getAliPolicy() {
            return policy;
        }

    }
}
