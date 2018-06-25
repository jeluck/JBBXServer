package com.jbb.server.core.domain;

import java.sql.Timestamp;

public class LoanPlatform {

    private int platformId;// 平台id
    private String name;// 平台名称
    private String description;// 平台描述
    private String minAmount;// 最高借款
    private String maxAmount;// 最高借款
    private String limitTime;// 借款期限
    private String loanRate;// 借款利息
    private String fastestTime;// 最快放款时间
    private String path;// 主页地址
    private String priority;// 推荐优先级
    private String serviceTelephone;// 服务号码
    private Boolean popular;// 是否热门
    private Boolean latest;// 是否最新
    private Boolean matched;// 是否匹配
    private Boolean borrowed;// 是否借过
    private Boolean delete;// 是否删除
    private Timestamp creationDate;// 创建时间

    public LoanPlatform() {

    }

    public LoanPlatform(int platformId, String name, String description) {
        this.platformId = platformId;
        this.name = name;
        this.description = description;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(String limitTime) {
        this.limitTime = limitTime;
    }

    public String getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(String loanRate) {
        this.loanRate = loanRate;
    }

    public String getFastestTime() {
        return fastestTime;
    }

    public void setFastestTime(String fastestTime) {
        this.fastestTime = fastestTime;
    }

  

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getServiceTelephone() {
        return serviceTelephone;
    }

    public void setServiceTelephone(String serviceTelephone) {
        this.serviceTelephone = serviceTelephone;
    }

    public Boolean isPopular() {
        return popular;
    }

    public void setPopular(Boolean popular) {
        this.popular = popular;
    }

    public Boolean isLatest() {
        return latest;
    }

    public void setLatest(Boolean latest) {
        this.latest = latest;
    }

    public Boolean isMatched() {
        return matched;
    }

    public void setMatched(Boolean matched) {
        this.matched = matched;
    }

    public Boolean isDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(Boolean borrowed) {
        this.borrowed = borrowed;
    }

    @Override
    public String toString() {
        return "LoanPlatforms [platformId=" + platformId + ", name=" + name + ", description=" + description
            + ", minAmount=" + minAmount+ ", maxAmount=" + maxAmount + ", limitTime=" + limitTime + ", loanRate=" + loanRate + ", fastestTime="
            + fastestTime +  ", path=" + path + ", priority=" + priority + ", serviceTelephone="
            + serviceTelephone  + ", popular=" + popular + ", latest=" + latest
            + ", matched=" + matched + ", borrowed=" + borrowed + ", delete=" + delete + ", creationDate="
            + creationDate + "]";
    }

}
