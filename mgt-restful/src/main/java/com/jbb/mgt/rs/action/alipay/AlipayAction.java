package com.jbb.mgt.rs.action.alipay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.service.AlipayService;
import com.jbb.mgt.rs.action.account.AccountAction;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.util.StringUtil;

@Service(AlipayAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AlipayAction extends BasicAction {

    public static final String ACTION_NAME = "AlipayAction";

    private static Logger logger = LoggerFactory.getLogger(AccountAction.class);

    private Response response;

    @Autowired
    private AlipayService alipayService;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        public String alipayForm;
        public int payResult;

    }

    public void payAliayOrder(int totalAmount, String goodsId, String outTradeNo, HttpServletResponse response) {
        logger.debug("> payAliayOrder(), totalAmount ={}, goodsId = {}, outTradeNo = {}", totalAmount, goodsId,
            outTradeNo);
        if (StringUtil.isEmpty(outTradeNo)) {
            throw new MissingParameterException("jbb.error.exception.wrong.paramvalue", "zh", "goodsId");
        }
        if (StringUtil.isEmpty(outTradeNo)) {
            throw new MissingParameterException("jbb.error.exception.wrong.paramvalue", "zh", "outTradeNo");
        }

        String form = alipayService.payAliayOrder(totalAmount, goodsId, outTradeNo, this.account.getAccountId());
        response.setContentType("text/html;charset=utf-8");
        try {
            response.getWriter().write(form);
            response.getWriter().flush();
        } catch (IOException e) {
            logger.error("write form error message = " + e.getMessage());
        } // 直接将完整的表单html输出到页面
        logger.debug("> payAliayOrder()");
    }

    public String notifyAlipayResult(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("> notifyAlipayResult()");
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String)iter.next();
            String[] values = (String[])requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);

        }
        boolean verify_result = false;
        String outTradeNo = "";
        String tradeStatus = "";
        String sellerId = "";
        String totalAmount = "";
        String tradeNo = "";
        try {
            outTradeNo = request.getParameter("out_trade_no");
            tradeStatus = request.getParameter("trade_status");
            sellerId = request.getParameter("seller_id");
            totalAmount = request.getParameter("total_amount");
            tradeNo = request.getParameter("trade_no");
            logger.debug("notify parameters,outTradeNo ={},tradeStatus={},sellerId={},totalAmount={},tradeNo={}",
                outTradeNo, tradeStatus, sellerId, totalAmount, tradeNo);
            String CHARSET = PropertyManager.getProperty("jbb.mgt.alipay.CHARSET");
            String ALIPAY_PUBLIC_KEY = PropertyManager.getProperty("jbb.mgt.alipay.PUBLICKEY");
            String SIGNTYPE = PropertyManager.getProperty("jbb.mgt.alipay.SIGNTYPE");
            verify_result = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, SIGNTYPE);
        } catch (Exception e) {
            logger.error("支付宝回调结果异常,异常原因{}", e.getMessage());
            e.printStackTrace();
        }
        String notifyOrder = "fail";
        if (verify_result) {
            // 表示验签成功
            if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS")) {// 交易成功 不可退款
                // 判断该笔订单是否在商户网站中已经做过处理
                // 再次通过query，反向获取订单信息，进行校验
                AlipayTradeQueryResponse alipayresponse = alipayService.queryAlipayResult(outTradeNo);
                totalAmount = alipayresponse.getTotalAmount();
                boolean result = alipayService.notifyAlipayResult(outTradeNo, totalAmount, sellerId, tradeNo);
                if (result == true) {
                    notifyOrder = "success";
                }
                // 注意：
                // 如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                // 如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
            }
        } else {
            logger.error("支付宝回调参数验签失败");
        }
        logger.debug("> notifyAlipayResult()");
        return notifyOrder;
    }

    public void queryAlipayResult(String outTradeNo) {
        logger.debug("> queryAlipayResult(), outTradeNo ={}", outTradeNo);
        if (StringUtil.isEmpty(outTradeNo)) {
            throw new MissingParameterException("jbb.error.exception.wrong.paramvalue", "zh", "outTradeNo");
        }
        AlipayTradeQueryResponse alipayresponse = alipayService.queryAlipayResult(outTradeNo);
        if (alipayresponse != null && alipayresponse.isSuccess()) {
            String tradeStatus = alipayresponse.getTradeStatus();
            logger.debug("tradeStatus " + tradeStatus);
            if (tradeStatus.equals("TRADE_SUCCESS") || tradeStatus.equals("TRADE_FINISHED")) {
                this.response.payResult = 1;
            } else {
                this.response.payResult = 0;// 处理中
            }
        } else {
            this.response.payResult = 0;// 处理中
        }
        logger.debug("> queryAlipayResult()");
    }

}
