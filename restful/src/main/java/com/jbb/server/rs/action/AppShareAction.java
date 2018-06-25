package com.jbb.server.rs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.core.domain.AppShareParams;
import com.jbb.server.rs.pojo.ActionResponse;

@Service(AppShareAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AppShareAction extends BasicAction {
    private static Logger logger = LoggerFactory.getLogger(AppShareAction.class);
    public static final String ACTION_NAME = "AppShareAction";

    private Response response;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    public void getAppShareConfigs() {
        logger.debug(">getAppShareConfigs");
        this.response.iosParams = generateParams("ios");
        this.response.androidParams = generateParams("android");
        this.response.defaultParams = generateParams("default");
        logger.debug("<getAppShareConfigs");
    }

    private AppShareParams generateParams(String platform) {
        String title = PropertyManager.getProperty("jbb.share." + platform + ".title");
        String desc = PropertyManager.getProperty("jbb.share." + platform + ".description");
        String image = PropertyManager.getProperty("jbb.share." + platform + ".image");
        String link = PropertyManager.getProperty("jbb.share." + platform + ".link");
        return new AppShareParams(title, desc, image, link);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private AppShareParams iosParams;
        private AppShareParams androidParams;
        private AppShareParams defaultParams;

        public AppShareParams getIosParams() {
            return iosParams;
        }

        public AppShareParams getAndroidParams() {
            return androidParams;
        }

        public AppShareParams getDefaultParams() {
            return defaultParams;
        }

    }
}
