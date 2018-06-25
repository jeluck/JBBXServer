package com.jbb.mgt.core.service.impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.jbb.mgt.core.dao.AccountDao;
import com.jbb.mgt.core.dao.OrgRechargeDetailDao;
import com.jbb.mgt.core.dao.OrgRechargesDao;
import com.jbb.mgt.core.dao.PayOrderDao;
import com.jbb.mgt.core.dao.mapper.PayOrderMapper;
import com.jbb.mgt.core.domain.Account;
import com.jbb.mgt.core.domain.OrgRechargeDetail;
import com.jbb.mgt.core.domain.OrgRecharges;
import com.jbb.mgt.core.domain.PayOrder;
import com.jbb.mgt.core.service.AlipayService;
import com.jbb.mgt.server.core.util.StringUtils;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.AliPayException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;

import net.sf.ehcache.constructs.scheduledrefresh.ScheduledRefreshConfiguration.PropKey;
import net.sf.json.JSONObject;

@Service("AlipayService")
public class AlipayServiceImpl implements AlipayService {

    private static Logger logger = LoggerFactory.getLogger(AlipayServiceImpl.class);
    private static DefaultTransactionDefinition NEW_TX_DEFINITION =
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

    private static String URL = PropertyManager.getProperty("jbb.mgt.alipay.URL");
    private static String APPID = PropertyManager.getProperty("jbb.mgt.alipay.APPID");
    private static String RSA_PRIVATE_KEY = PropertyManager.getProperty("jbb.mgt.alipay.PRIVATEKEY");
    private static String FORMAT = PropertyManager.getProperty("jbb.mgt.alipay.FORMAT");
    private static String CHARSET = PropertyManager.getProperty("jbb.mgt.alipay.CHARSET");
    private static String ALIPAY_PUBLIC_KEY = PropertyManager.getProperty("jbb.mgt.alipay.PUBLICKEY");
    private static String SIGNTYPE = PropertyManager.getProperty("jbb.mgt.alipay.SIGNTYPE");

    private static AlipayClient client =
        new DefaultAlipayClient(URL, APPID, RSA_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGNTYPE);

    @Autowired
    private PayOrderDao payOrderDao;
    @Autowired
    private OrgRechargesDao orgRechargesDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    private OrgRechargeDetailDao orgRechargeDetailDao;

    @Override
    public String payAliayOrder(int totalAmount, String goodsId, String outTradeNo, int userId) {
        double amount = (double)totalAmount / 100;
        java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.00");
        Map<String, String> maps = new HashMap<String, String>();
        PayOrder ordercheck = payOrderDao.selectPayOrder(outTradeNo);
        if (ordercheck != null) {
            throw new WrongParameterValueException("jbb.error.exception.alipay.outTradNoExist", "zh", "outTradeNo");
        }
        String subject = PropertyManager.getProperty("jbb.mgt.goods." + goodsId + ".subject");
        String body = PropertyManager.getProperty("jbb.mgt.goods." + goodsId + ".detail");
        String returnUrl = PropertyManager.getProperty("jbb.mgt.alipay.return.url");
        String notifyUrl = PropertyManager.getProperty("jbb.mgt.alipay.notify.url");
        maps.put("out_trade_no", outTradeNo);
        logger.debug("amount " + amount);
        maps.put("total_amount", myformat.format(amount));
        maps.put("subject", subject);
        maps.put("body", body);
        maps.put("product_code", "FAST_INSTANT_TRADE_PAY");// 电脑网页参数

        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();// 创建API对应的request
        alipayRequest.setReturnUrl(returnUrl);
        alipayRequest.setNotifyUrl(notifyUrl);// 在公共参数中设置回跳和通知地址
        String bizCon = JSON.toJSONString(maps);
        alipayRequest.setBizContent(bizCon);
        String form = "";
        try {
            form = client.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
            if (!StringUtil.isEmpty(form)) {
                // 获取form之后 生成payorder
                PayOrder payOrder = new PayOrder();
                payOrder.setOutTradeNo(outTradeNo);
                payOrder.setPayMoney(totalAmount);
                payOrder.setPayGoods(body);
                payOrder.setUserId(userId);
                payOrder.setSubject(subject);
                payOrder.setPayStatus(0);
                payOrder.setGoodsId(goodsId);
                payOrder.setCreationDate(DateUtil.getCurrentTimeStamp());
                payOrder.setPayWay(1);
                payOrderDao.insertPayOrder(payOrder);
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝支付失败！订单号：{},原因:{}", outTradeNo, e.getMessage());
            logger.debug("<payAliayOrder with fail");
        }
        return form;
    }

    @Override
    public boolean notifyAlipayResult(String outTradeNo, String totalAmount, String sellerId, String tradeNo) {
        TransactionStatus txStatus = null;
        boolean flag = false;
        String sellerIdCOnfig = PropertyManager.getProperty("jbb.mgt.alipay.SELLERID");
        try {
            txStatus = txManager.getTransaction(NEW_TX_DEFINITION);
            // 用自身的订单号,不是支付宝提供的流水号
            PayOrder payOrder = payOrderDao.selectPayOrderForUpdate(outTradeNo);

            // 校验订单是否已经被处理过
            if (payOrder != null) {
                Timestamp payDate = payOrder.getPayDate();
                if (payDate != null && payDate.getTime() <= DateUtil.getCurrentTime() && payOrder.getPayStatus() == 1) {
                    txStatus = null;
                    return true;
                } else {
                    // 检验订单信息是否完全一致
                    int totalAmountint = (int)(Double.valueOf(totalAmount) * 100);
                    if (totalAmountint != payOrder.getPayMoney()) {
                        logger.warn("notify totalamount not equals order totalamount");
                        return false;
                    }
                    if (!sellerId.equals(sellerIdCOnfig)) {
                        logger.warn("notify sellerId not equals order sellerId");
                        return false;
                    }
                    // 记录处理，增加付款时间，修改信息为已付款
                    payOrderDao.updatePayOrder(outTradeNo, 1, DateUtil.getCurrentTimeStamp(), tradeNo);
                    Account account = accountDao.selectAccountById(payOrder.getUserId(), null);
                    if (account == null) {
                        logger.error("支付宝查询失败 ！订单号{},原因:{}", payOrder.getUserId());
                        return false;
                    }
                    OrgRecharges o = orgRechargesDao.selectOrgRechargesForUpdate(account.getOrgId());
                    if (o == null) {
                        logger.error("支付宝查询失败 ！订单号{},原因:{}", account.getOrgId());
                        return false;
                    }
                    // 加钱之前，根据费率进行换算，加钱数 = 总金额/(1+费率)
                    float poundage = PropertyManager.getFloatProperty("jbb.mgt.alipay.poundage", (float)0.011);
                    int money = (int)(payOrder.getPayMoney() / (1 + poundage));
                    OrgRechargeDetail orgRechargeDetail = new OrgRechargeDetail();
                    if (payOrder.getGoodsId().equals("1")) {
                        int totalSmsAmount = o.getTotalSmsAmount() + money;
                        o.setTotalSmsAmount(totalSmsAmount);
                        orgRechargeDetail.setOpType(11);// 短信费
                    } else if (payOrder.getGoodsId().equals("2")) {
                        int totalDataAmount = o.getTotalDataAmount() + money;
                        o.setTotalDataAmount(totalDataAmount);
                        orgRechargeDetail.setOpType(12);// 流量费
                    }
                    orgRechargesDao.updateOrgRecharges(o);
                    // 加钱之后，增加交易记录详情表
                    orgRechargeDetail.setAccountId(account.getAccountId());
                    orgRechargeDetail.setAmount(money);
                    orgRechargeDetail.setCreationDate(DateUtil.getCurrentTimeStamp());
                    orgRechargeDetail.setStatus(1);
                    orgRechargeDetail.setTradeNo(StringUtils.getRandomString(32));
                    Map<String, Object> mapInfo = new HashMap<>();
                    mapInfo.put("outTradeNo", outTradeNo);
                    mapInfo.put("tradeDate", DateUtil.getCurrentTime());
                    mapInfo.put("sellerId", sellerId);
                    mapInfo.put("tradeNo", tradeNo);
                    JSONObject jsonObj = JSONObject.fromObject(mapInfo);
                    orgRechargeDetail.setParams(jsonObj.toString());
                    orgRechargeDetailDao.insertOrgRechargeDetail(orgRechargeDetail);

                    logger.debug("money has been added");
                }
                txManager.commit(txStatus);
                txStatus = null;
                flag = true;
            }

        } finally {
            // roll back not committed transaction (release user lock)
            rollbackTransaction(txStatus);
        }
        return flag;
    }

    @Override
    public AlipayTradeQueryResponse queryAlipayResult(String outTradeNo) {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("out_trade_no", outTradeNo);
        String bizCon = JSON.toJSONString(maps);
        AlipayTradeQueryResponse responseAlipay = null;
        try {
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            request.setBizContent(bizCon);
            responseAlipay = client.execute(request);
        } catch (AlipayApiException e) {
            logger.error("支付宝查询失败 ！订单号{},原因:{}", outTradeNo, e.getMessage());
            throw new AliPayException("jbb.error.exception.alipay.error", e.getMessage());
        }
        return responseAlipay;
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

}
