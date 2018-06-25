package com.jbb.mgt.core.service;

import com.alipay.api.response.AlipayTradeQueryResponse;

public interface AlipayService {

    String payAliayOrder(int totalAmount, String goodsId, String outTradeNo, int userId);

    boolean notifyAlipayResult(String outTradeNo, String totalAmount, String seller_id, String tradeNo);

    AlipayTradeQueryResponse queryAlipayResult(String outTradeNo);
}
