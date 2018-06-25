
package com.jbb.server.rs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.rs.pojo.ActionResponse;

@Service(LeanCloudInfoAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeanCloudInfoAction extends BasicAction {
    private static Logger logger = LoggerFactory.getLogger(LeanCloudInfoAction.class);
    public static final String ACTION_NAME = "LeanCloudInfoAction";
    
    private Response response;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    public void readLeanCloudInfo() {
        logger.debug(">readLeanCloudInfo");
        this.response.appId = PropertyManager.getProperty("jbb.msg.leancloud.appId");
        this.response.appKey = PropertyManager.getProperty("jbb.msg.leancloud.appKey");
        logger.debug("<readLeanCloudInfo");
       
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private String appId;
        private String appKey;
        public String getAppId() {
            return appId;
        }
        public String getAppKey() {
            return appKey;
        }

       
    }
}
