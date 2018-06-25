package com.jbb.mgt.core.dao.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.PayOrderDao;
import com.jbb.mgt.core.dao.mapper.PayOrderMapper;
import com.jbb.mgt.core.domain.PayOrder;

@Repository("PayOrderDao")
public class PayOrderDaoImpl implements PayOrderDao {

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Override
    public boolean insertPayOrder(PayOrder payOrder) {
        return payOrderMapper.insertPayOrder(payOrder) > 0;
    }

    @Override
    public boolean updatePayOrder(String outTradeNo, int payStatus, Timestamp payDate,String tradeNo) {
        return payOrderMapper.updatePayOrder(outTradeNo, payStatus, payDate,tradeNo) > 0;
    }

    @Override
    public PayOrder selectPayOrder(String outTradeNo) {
        return payOrderMapper.selectPayOrder(outTradeNo);
    }
    
    @Override
    public PayOrder selectPayOrderForUpdate(String outTradeNo) {
        return payOrderMapper.selectPayOrderForUpdate(outTradeNo);
    }

}
