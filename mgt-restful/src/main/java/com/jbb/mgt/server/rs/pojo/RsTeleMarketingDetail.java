package com.jbb.mgt.server.rs.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.TeleMarketingDetail;
import com.jbb.server.shared.rs.Util;

/**
 * 批次明细数据response实体类
 * 
 * @author wyq
 * @date 2018/04/30
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsTeleMarketingDetail {

    private String mobile;
    private String username;
    private String area;
    private String numberType;
    private Long lastTime;
    private int status;
    private RsAccount account;

    public RsTeleMarketingDetail(TeleMarketingDetail detail) {
        this.mobile = detail.getTelephone();
        this.username = detail.getUsername();
        this.area = detail.getArea();
        this.numberType = detail.getNumberType();
        this.lastTime = Util.getTimeMs(detail.getLastDate());
        this.status = detail.getStatus();
        if (null != detail.getAccount()) {
            this.account = new RsAccount();
            this.account.setAccountId(detail.getAccount().getAccountId());
            this.account.setUsername(detail.getAccount().getUsername());
            this.account.setNickname(detail.getAccount().getNickname());
        }
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public RsAccount getAccount() {
        return account;
    }

    public void setAccount(RsAccount account) {
        this.account = account;
    }

    public RsTeleMarketingDetail() {
        super();
    }

}
