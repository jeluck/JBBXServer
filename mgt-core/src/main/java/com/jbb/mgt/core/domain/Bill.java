package com.jbb.mgt.core.domain;

public class Bill {

    private String type; // 类型: 短信，流量
    private int totalAmount; // 总数
    private int buget; // 余额
    private int expense; // 消费额

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getBuget() {
        return buget;
    }

    public void setBuget(int buget) {
        this.buget = buget;
    }

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }
}
