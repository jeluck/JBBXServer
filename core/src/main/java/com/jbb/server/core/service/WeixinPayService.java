package com.jbb.server.core.service;

import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;

public interface WeixinPayService {

    /**
     * 统一下单 :https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1
     * 
     * @param productName :商品名字串
     * 
     */
    WxPayAppOrderResult unifiedorder(int userId, String productName, String orderNo, String ipAddress, String attach);

    /**
     * 支付结果通知：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_7
     */
    String notifyPayResult(String xmlResult);

    /**
     * 查询订单
     * 
     * @param transactionId
     * @param outTradeNo
     * @return
     */
    WxPayOrderQueryResult queryOrder(String transactionId, String outTradeNo);

}
