package com.jbb.server.rs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.jbb.server.core.service.WeixinPayService;
import com.jbb.server.rs.action.UserPropertiesAction.Response;
import com.jbb.server.rs.pojo.ActionResponse;

@Service(WxQueryOrderAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WxQueryOrderAction extends BasicAction {

    private static Logger logger = LoggerFactory.getLogger(WxQueryOrderAction.class);

    public static final String ACTION_NAME = "WxQueryOrderAction";

    private Response response;
    @Autowired
    private WeixinPayService weixinPayService;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    public void queryOrder(String transactionId, String outTradeNo) {
        logger.info(">queryOrder, transactionId=" + transactionId + ",outTradeNo" + outTradeNo);

        WxPayOrderQueryResult result = weixinPayService.queryOrder(transactionId, outTradeNo);
        this.response.tradeState = result.getTradeState();
        this.response.totalFee = result.getTotalFee();

        logger.info("<unifiedorder");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private String tradeState;
        private Integer totalFee;

        public String getTradeState() {
            return tradeState;
        }

        public Integer getTotalFee() {
            return totalFee;
        }

    }

}
