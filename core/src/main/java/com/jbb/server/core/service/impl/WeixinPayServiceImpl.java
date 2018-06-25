package com.jbb.server.core.service.impl;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.WxPayException;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.dao.OrdersDao;
import com.jbb.server.core.domain.Order;
import com.jbb.server.core.service.WeixinPayService;

@Service("weixinPayService")
public class WeixinPayServiceImpl implements WeixinPayService {
    private static Logger logger = LoggerFactory.getLogger(WeixinPayServiceImpl.class);

    private static DefaultTransactionDefinition NEW_TX_DEFINITION =
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    private OrdersDao orderDao;

    @Override
    public WxPayAppOrderResult unifiedorder(int userId, String productName, String orderNo, String ipAddress,
        String attach) {

        logger.debug(">unifiedorder , userId = " + userId + ", productName=" + productName + ", ipAddress" + ipAddress);

        TransactionStatus txStatus = null;
        try {
            txStatus = txManager.getTransaction(NEW_TX_DEFINITION);
            WxPayService wxPayService = createWxPayService();
            String notifyUrl = PropertyManager.getProperty("jbb.wx.pay.notify.url");
           // String name = PropertyManager.getProperty("jbb.wx.pay." + productName + ".name");
            String body = PropertyManager.getProperty("jbb.wx.pay." + productName + ".body");
            String detail = PropertyManager.getProperty("jbb.wx.pay." + productName + ".detail");
            int fee = PropertyManager.getIntProperty("jbb.wx.pay." + productName + ".fee", 800);

            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setNotifyUrl(notifyUrl);
            orderRequest.setBody(body);
            orderRequest.setOutTradeNo(orderNo);
            orderRequest.setTotalFee(fee);
            orderRequest.setSpbillCreateIp(ipAddress);
            orderRequest.setDeviceInfo("WEB");
            orderRequest.setAttach(attach);
            orderRequest.setDetail(detail);
            // orderRequest.setTimeStart("20180408220000");
            // orderRequest.setTimeExpire("20180408220500");
            WxPayAppOrderResult result = wxPayService.createOrder(orderRequest);
            logger.debug("<unifiedorder");

            // 插入订单记录
            Order order = new Order(orderRequest.getOutTradeNo(), productName, fee, ipAddress, "weixin",
                DateUtil.getCurrentTimeStamp(), userId);

            orderDao.insertOrder(order);

            try {
                orderDao.insertUserProductCount(userId, productName, 0);
            } catch (DuplicateKeyException e) {
                // ignore , nothing to do
            }

            txManager.commit(txStatus);
            txStatus = null;

            logger.debug("<unifiedorder");
            return result;
        } catch (Exception e) {
            logger.error("微信支付失败！订单号：{},原因:{}", orderNo, e.getMessage());
            e.printStackTrace();
            logger.debug("<unifiedorder with fail");
            throw new WxPayException("jbb.error.exception.wxpay.error", e.getMessage());
        } finally {
            // roll back not committed transaction
            rollbackTransaction(txStatus);
        }
    }

    private void rollbackTransaction(TransactionStatus txStatus) {
        if (txStatus == null) {
            return;
        }

        try {
            txManager.rollback(txStatus);
        } catch (Exception er) {
            logger.warn("Cannot rollback transaction", er);
        }
    }

    @Override
    public String notifyPayResult(String xmlResult) {
        logger.debug(">notifyPayResult , xmlResult = " + xmlResult);

        WxPayService wxPayService = createWxPayService();
        TransactionStatus txStatus = null;

        try {
            txStatus = txManager.getTransaction(NEW_TX_DEFINITION);

            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);
            // 结果正确
            String orderId = result.getOutTradeNo();
            // String tradeNo = result.getTransactionId();
            // int totalFee = result.getTotalFee();
            // 自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用

            Order order = orderDao.selectOrder(orderId);

            if (order != null) {
                Timestamp payDate = order.getPayDate();
                if (payDate != null && payDate.getTime() <= DateUtil.getCurrentTime()) {
                    // 已经处理
                    // nothing to do
                } else {
                    // 记录处理，并且增加用户相关权益次数
                    String timeEnd = result.getTimeEnd();
                    order.setPayDate(DateUtil.parseStr(timeEnd));
                    orderDao.selectUserProductCountForUpdate(order.getUserId(), order.getProductName());
                    orderDao.upadateOrder(order);
                    orderDao.increaseUserProductCount(order.getUserId(), order.getProductName());

                }
            } else {
                // noting to do, can't find order in JBB system
                logger.warn("not find order in JBB system, orderId = " + orderId);
            }

            txManager.commit(txStatus);
            txStatus = null;
            return WxPayNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            logger.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        } finally {
            // roll back not committed transaction
            rollbackTransaction(txStatus);
        }

    }

    @Override
    public WxPayOrderQueryResult queryOrder(String transactionId, String outTradeNo) {
        logger.debug(">queryOrder , transactionId = " + transactionId + ",outTradeNo=" + outTradeNo);
        WxPayService wxPayService = createWxPayService();
        try {
            WxPayOrderQueryResult result = wxPayService.queryOrder(transactionId, outTradeNo);
            logger.debug("<queryOrder");
            return result;
        } catch (Exception e) {
            logger.error("微信回调结果异常,异常原因{}", e.getMessage());
            throw new WxPayException("jbb.error.exception.wxpay.error", e.getMessage());
        }
    }

    private WxPayService createWxPayService() {
        WxPayConfig config = buildWxPayConfig();
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(config);
        return wxPayService;
    }

    private WxPayConfig buildWxPayConfig() {
        WxPayConfig config = new WxPayConfig();
        config.setAppId(PropertyManager.getProperty("jbb.wx.pay.appid"));
        config.setMchId(PropertyManager.getProperty("jbb.wx.pay.mchid"));
        config.setMchKey(PropertyManager.getProperty("jbb.wx.pay.mchkey"));
        // config.setKeyPath(keyPath);
        config.setTradeType("APP");
        config.setSignType(WxPayConstants.SignType.MD5);
        return config;
    }
}
