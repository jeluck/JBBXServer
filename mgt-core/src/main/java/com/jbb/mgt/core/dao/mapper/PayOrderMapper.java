package com.jbb.mgt.core.dao.mapper;

import java.sql.Timestamp;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.PayOrder;

public interface PayOrderMapper {

    int insertPayOrder(PayOrder payOrder);

    int updatePayOrder(@Param(value = "outTradeNo") String outTradeNo, @Param(value = "payStatus") int payStatus,
        @Param(value = "payDate") Timestamp payDate,@Param(value = "tradeNo") String tradeNo);

    PayOrder selectPayOrder(@Param(value = "outTradeNo") String outTradeNo);

    PayOrder selectPayOrderForUpdate(@Param(value = "outTradeNo") String outTradeNo);

}
