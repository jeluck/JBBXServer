
package com.jbb.server.rs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.domain.Fee;
import com.jbb.server.core.service.ProductService;
import com.jbb.server.core.service.WeixinPayService;
import com.jbb.server.rs.pojo.ActionResponse;

@Component(PlatformAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PlatformAction extends BasicAction {
    private static Logger logger = LoggerFactory.getLogger(PlatformAction.class);
    public static final String ACTION_NAME = "PlatformAction";

    private Response response;

    @Autowired
    private ProductService productService;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }
    
    public void getPlatformInfo(){
        this.response.currentIosEnv = PropertyManager.getProperty("jbb.platform.ios");
    }

    public void getPlatformFees(String name) {
        logger.debug(">getPlatformFees(), name =" + name);

        String displayName = PropertyManager.getProperty("jbb.wx.pay." + name + ".name");
        String body = PropertyManager.getProperty("jbb.wx.pay." + name + ".body");
        String detail = PropertyManager.getProperty("jbb.wx.pay." + name + ".detail");
        int fee = PropertyManager.getIntProperty("jbb.wx.pay." + name + ".fee", 0);
        if (!StringUtil.isEmpty(displayName)) {
            this.response.fee = new Fee(displayName, body, detail, fee);
            this.response.count = productService.getProductCount(this.user.getUserId(), name);
        }else{
            this.response.count = 0;
        }
        logger.debug("<getPlatformFees()");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        
        private String currentIosEnv ;
        private Fee fee;
        private Integer count;

        public Fee getFee() {
            return fee;
        }

        public Integer getCount() {
            return count;
        }

        public String getCurrentIosEnv() {
            return currentIosEnv;
        }
        
        

    }
}
