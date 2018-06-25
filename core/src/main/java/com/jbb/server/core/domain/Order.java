package com.jbb.server.core.domain;

import java.sql.Timestamp;

public class Order {

    private String orderNo;
    private String productName;
    private int totalFee;
    private String ipAddress;
    private String payType;
    private Timestamp creationDate;
    private Timestamp payDate;
    private int userId;
    
    
    public Order() {
        super();
    }

    public Order(String orderNo, String productName, int totalFee, String ipAddress, String payType,
        Timestamp creationDate, int userId) {
        super();
        this.orderNo = orderNo;
        this.productName = productName;
        this.totalFee = totalFee;
        this.ipAddress = ipAddress;
        this.payType = payType;
        this.creationDate = creationDate;
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Orders [orderNo=" + orderNo + ", product_name=" + productName + ", totalFee=" + totalFee
            + ", ipAddress=" + ipAddress + ", payType=" + payType + ", creationDate=" + creationDate + ", payDate="
            + payDate + ", userId=" + userId + "]";
    }

}
