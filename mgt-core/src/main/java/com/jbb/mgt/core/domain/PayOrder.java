package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

/**
 * @author szy 支付订单
 */
public class PayOrder {

    private String outTradeNo;// 订单号
    private String tradeNo;// 订单号
    private String goodsId;
    private int userId;
    private int payMoney; // 支付金额
    private String payGoods; // 支付的商品名称
    private int payStatus;// 支付状态 0：未支付 1：支付成功
    private Timestamp creationDate;
    private Timestamp payDate;// 支付时间
    private int payWay;// 支付方式 1：支付宝 2：微信
    private String subject;

    public PayOrder() {
        super();
    }

    public PayOrder(String outTradeNo, String tradeNo, String goodsId, int userId, int payMoney, String payGoods,
        int payStatus, Timestamp creationDate, Timestamp payDate, int payWay, String subject) {
        super();
        this.outTradeNo = outTradeNo;
        this.tradeNo = tradeNo;
        this.goodsId = goodsId;
        this.userId = userId;
        this.payMoney = payMoney;
        this.payGoods = payGoods;
        this.payStatus = payStatus;
        this.creationDate = creationDate;
        this.payDate = payDate;
        this.payWay = payWay;
        this.subject = subject;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public int getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(int payMoney) {
        this.payMoney = payMoney;
    }

    public String getPayGoods() {
        return payGoods;
    }

    public void setPayGoods(String payGoods) {
        this.payGoods = payGoods;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getPayDate() {
        return payDate;
    }

    public void setPayDate(Timestamp payDate) {
        this.payDate = payDate;
    }

    @Override
    public String toString() {
        return "PayOrder [outTradeNo=" + outTradeNo + ", tradeNo=" + tradeNo + ", userId=" + userId + ", payMoney="
            + payMoney + ", payGoods=" + payGoods + ", payStatus=" + payStatus + ", creationDate=" + creationDate
            + ", payDate=" + payDate + ", payWay=" + payWay + ", subject=" + subject + "]";
    }

}
