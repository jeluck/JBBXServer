package com.jbb.mgt.core.dao;

import java.sql.Timestamp;

import com.jbb.mgt.core.domain.PayOrder;

public interface PayOrderDao {

    boolean insertPayOrder(PayOrder payOrder);

    boolean updatePayOrder(String outTradeNo, int payStatus, Timestamp payDate,String tradeNo);

    PayOrder selectPayOrder(String outTradeNo);
    
    PayOrder selectPayOrderForUpdate(String outTradeNo);

}
