package com.jbb.server.rs.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.domain.Advertising;
import com.jbb.server.core.service.AdvertisingService;
import com.jbb.server.rs.pojo.ActionResponse;

@Service(AdvertisingAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AdvertisingAction extends BasicAction {

    public static String AD_PLATFORM_DEFAULT = "Web";
    private static Logger logger = LoggerFactory.getLogger(AdvertisingAction.class);
    public static final String ACTION_NAME = "AdvertisingAction";
    @Autowired
    private AdvertisingService advertisingService;
    private Response response;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    public void getAdvertising(String platform) {
        logger.debug(">getAdvertising(), platfrom = " + platform);
        if (StringUtil.isEmpty(platform)) {
            platform = AD_PLATFORM_DEFAULT; // Default Value
        }
        this.response.advertising = advertisingService.getAdvertising(platform);
        this.response.pictureServerUrl = PropertyManager.getProperty("jbb.picture.server.address");
        this.response.path = PropertyManager.getProperty("jbb.picture.server.ads.path");
        this.response.apkServerUrl = PropertyManager.getProperty("jbb.apk.server.address");
        
        logger.debug("<getAdvertising()");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private List<Advertising> advertising;

        private String pictureServerUrl;
        private String path;
        private String apkServerUrl;

        @JsonProperty("ads")
        public List<Advertising> getAdvertising() {
            return advertising;
        }

        public String getPictureServerUrl() {
            return pictureServerUrl;
        }

        public String getApkServerUrl() {
            return apkServerUrl;
        }

        public String getPath() {
            return path;
        }
    }
}
