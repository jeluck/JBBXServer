package com.jbb.server.rs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.common.Constants;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.ApiCallLimitException;
import com.jbb.server.core.domain.IdCard;
import com.jbb.server.core.service.AliyunService;
import com.jbb.server.core.service.ProductService;
import com.jbb.server.rs.pojo.ActionResponse;

@Component(OcrIdCardAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OcrIdCardAction extends BasicAction {
    private static Logger logger = LoggerFactory.getLogger(OcrIdCardAction.class);
    public static final String ACTION_NAME = "OcrIdCardAction";

    private Response response;
    
    @Autowired
    private ProductService productService;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    @Autowired
    private AliyunService aliyunService;

    public void ocrIdCard(Object body) {
        logger.debug(">ocrIdCard()");
        int fee = PropertyManager.getIntProperty("jbb.wx.pay.auth.fee", 0);
        if (fee !=0 && productService.getProductCount(this.user.getUserId(), Constants.PRODUCT_AUTH) == 0) {
            throw new ApiCallLimitException("jbb.error.exception.auth.notPay");
        }

        this.response.idcard = aliyunService.ocrIdCard(this.user.getUserId(),body);

        logger.debug("<ocrIdCard()");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {

        private IdCard idcard;

        public IdCard getIdcard() {
            return idcard;
        }

    }
}
