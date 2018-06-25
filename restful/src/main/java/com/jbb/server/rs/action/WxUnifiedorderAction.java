package com.jbb.server.rs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.service.WeixinPayService;
import com.jbb.server.rs.pojo.ActionResponse;

@Service(WxUnifiedorderAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WxUnifiedorderAction extends BasicAction {

    private static Logger logger = LoggerFactory.getLogger(WxUnifiedorderAction.class);

    public static final String ACTION_NAME = "WxUnifiedorderAction";

    private Response response;
    @Autowired
    private WeixinPayService weixinPayService;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    public void unifiedorder(String productName, String attach) {
        logger.info(">unifiedorder, productName=" + productName);
        String orderNo = StringUtil.randomAlphaNum(32);
        WxPayAppOrderResult result =
            weixinPayService.unifiedorder(this.user.getUserId(), productName, orderNo, this.getRemoteAddress(), attach);
        this.response.appId = result.getAppId();
        this.response.nonceStr = result.getNonceStr();
        this.response.prepayId = result.getPrepayId();
        this.response.partnerId = result.getPartnerId();
        this.response.sign = result.getSign();
        this.response.timestamp = result.getTimeStamp();
        this.response.orderNo = orderNo;

        logger.info("<unifiedorder");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private String appId;
        private String nonceStr;
        private String prepayId;
        private String partnerId;
        private String orderNo;
        private String sign;
        private String timestamp;

        public String getAppId() {
            return appId;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public String getPrepayId() {
            return prepayId;
        }

        public String getPartnerId() {
            return partnerId;
        }

        public String getSign() {
            return sign;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getOrderNo() {
            return orderNo;
        }

    }

}
