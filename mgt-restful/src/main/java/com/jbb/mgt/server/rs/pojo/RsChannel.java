package com.jbb.mgt.server.rs.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.Channel;
import com.jbb.mgt.core.domain.ChannelStatistic;
import com.jbb.server.shared.rs.Util;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsChannel {
    private String channelCode;
    private String channelName;
    private String channelUrl;
    private String serviceQQ;
    private String serviceWechat;
    private int status;
    private int mode;
    private String sourcePhoneNumber;
    private Long creationDate;
    private boolean qqRequired;
    private boolean wechatRequired;
    private boolean zhimaRequired;
    private boolean idcardInfoRequired;
    private boolean idcardBackRequired;
    private boolean idcardRearRequired;
    private boolean headerRequired;
    private boolean mobileContract1Required;
    private boolean mobileContract2Required;
    private boolean mobileServiceInfoRequired;
    private int cpaPrice;
    private int cpsPrice;
    private int receiveMode;
    private boolean hidden;
    private boolean taobaoRequired;
    private boolean jingdongRequired;
    private boolean gjjRequired;
    private boolean sjRequired;
    private boolean chsiRequired;
    private RsAccount creator;
    private ChannelStatistic statistic;

    public RsChannel() {

    }

    public RsChannel(Channel channel) {
        this.channelCode = channel.getChannelCode();
        this.channelName = channel.getChannelName();
        this.channelUrl = channel.getChannelUrl();
        this.serviceQQ = channel.getServiceQQ();
        this.serviceWechat = channel.getServiceWechat();
        this.status = channel.getStatus();
        this.mode = channel.getMode();
        this.sourcePhoneNumber = channel.getSourcePhoneNumber();
        this.creationDate = Util.getTimeMs(channel.getCreationDate());
        this.qqRequired = channel.isQqRequired();
        this.wechatRequired = channel.isWechatRequired();
        this.zhimaRequired = channel.isZhimaRequired();
        this.idcardInfoRequired = channel.isIdcardInfoRequired();
        this.idcardBackRequired = channel.isIdcardBackRequired();
        this.idcardRearRequired = channel.isIdcardRearRequired();
        this.headerRequired = channel.isHeaderRequired();
        this.mobileContract1Required = channel.isMobileContract1Required();
        this.mobileContract2Required = channel.isMobileContract2Required();
        this.mobileServiceInfoRequired = channel.isMobileServiceInfoRequired();
        this.cpaPrice = channel.getCpaPrice();
        this.cpsPrice = channel.getCpsPrice();
        this.receiveMode = channel.getReceiveMode();
        this.hidden = channel.isHidden();
        this.taobaoRequired = channel.isTaobaoRequired();
        this.jingdongRequired = channel.isJingdongRequired();
        this.gjjRequired = channel.isGjjRequired();
        this.sjRequired = channel.isSjRequired();
        this.chsiRequired = channel.isChsiRequired();
        if (null != channel.getAccount()) {
            this.creator = new RsAccount();
            this.creator.setAccountId(channel.getAccount().getAccountId());
            this.creator.setUsername(channel.getAccount().getUsername());
            this.creator.setNickname(channel.getAccount().getNickname());
            this.creator.setPhoneNumber(channel.getAccount().getPhoneNumber());
            this.creator.setJbbUserId(channel.getAccount().getJbbUserId());
            this.creator.setPassword(channel.getAccount().getPassword());
            this.creator.setOrgId(channel.getAccount().getOrgId());
            this.creator.setCreator(channel.getAccount().getCreator());
            this.creator.setCreationDate(Util.getTimeMs(channel.getAccount().getCreationDate()));
            this.creator.setDeleted(channel.getAccount().isDeleted());
            this.creator.setFreeze(channel.getAccount().isFreeze());
        }
        if (null != channel.getStatistic()) {
            this.statistic = new ChannelStatistic();
            this.statistic.setClickCnt(channel.getStatistic().getClickCnt());
            this.statistic.setLoanCnt(channel.getStatistic().getLoanCnt());
            this.statistic.setRegisterCnt(channel.getStatistic().getRegisterCnt());
            this.statistic.setSubmitCnt(channel.getStatistic().getSubmitCnt());
            this.statistic.setUvCnt(channel.getStatistic().getUvCnt());
        }
    }

    public int getReceiveMode() {
        return receiveMode;
    }

    public void setReceiveMode(int receiveMode) {
        this.receiveMode = receiveMode;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public String getServiceQQ() {
        return serviceQQ;
    }

    public void setServiceQQ(String serviceQQ) {
        this.serviceQQ = serviceQQ;
    }

    public String getServiceWechat() {
        return serviceWechat;
    }

    public void setServiceWechat(String serviceWechat) {
        this.serviceWechat = serviceWechat;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getSourcePhoneNumber() {
        return sourcePhoneNumber;
    }

    public void setSourcePhoneNumber(String sourcePhoneNumber) {
        this.sourcePhoneNumber = sourcePhoneNumber;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isQqRequired() {
        return qqRequired;
    }

    public void setQqRequired(boolean qqRequired) {
        this.qqRequired = qqRequired;
    }

    public boolean isWechatRequired() {
        return wechatRequired;
    }

    public void setWechatRequired(boolean wechatRequired) {
        this.wechatRequired = wechatRequired;
    }

    public boolean isZhimaRequired() {
        return zhimaRequired;
    }

    public void setZhimaRequired(boolean zhimaRequired) {
        this.zhimaRequired = zhimaRequired;
    }

    public boolean isIdcardInfoRequired() {
        return idcardInfoRequired;
    }

    public void setIdcardInfoRequired(boolean idcardInfoRequired) {
        this.idcardInfoRequired = idcardInfoRequired;
    }

    public boolean isIdcardBackRequired() {
        return idcardBackRequired;
    }

    public void setIdcardBackRequired(boolean idcardBackRequired) {
        this.idcardBackRequired = idcardBackRequired;
    }

    public boolean isIdcardRearRequired() {
        return idcardRearRequired;
    }

    public void setIdcardRearRequired(boolean idcardRearRequired) {
        this.idcardRearRequired = idcardRearRequired;
    }

    public boolean isHeaderRequired() {
        return headerRequired;
    }

    public void setHeaderRequired(boolean headerRequired) {
        this.headerRequired = headerRequired;
    }

    public boolean isMobileContract1Required() {
        return mobileContract1Required;
    }

    public void setMobileContract1Required(boolean mobileContract1Required) {
        this.mobileContract1Required = mobileContract1Required;
    }

    public boolean isMobileContract2Required() {
        return mobileContract2Required;
    }

    public void setMobileContract2Required(boolean mobileContract2Required) {
        this.mobileContract2Required = mobileContract2Required;
    }

    public boolean isMobileServiceInfoRequired() {
        return mobileServiceInfoRequired;
    }

    public void setMobileServiceInfoRequired(boolean mobileServiceInfoRequired) {
        this.mobileServiceInfoRequired = mobileServiceInfoRequired;
    }

    public int getCpaPrice() {
        return cpaPrice;
    }

    public void setCpaPrice(int cpaPrice) {
        this.cpaPrice = cpaPrice;
    }

    public int getCpsPrice() {
        return cpsPrice;
    }

    public void setCpsPrice(int cpsPrice) {
        this.cpsPrice = cpsPrice;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isTaobaoRequired() {
        return taobaoRequired;
    }

    public void setTaobaoRequired(boolean taobaoRequired) {
        this.taobaoRequired = taobaoRequired;
    }

    public boolean isJingdongRequired() {
        return jingdongRequired;
    }

    public void setJingdongRequired(boolean jingdongRequired) {
        this.jingdongRequired = jingdongRequired;
    }

    public boolean isGjjRequired() {
        return gjjRequired;
    }

    public void setGjjRequired(boolean gjjRequired) {
        this.gjjRequired = gjjRequired;
    }

    public boolean isSjRequired() {
        return sjRequired;
    }

    public void setSjRequired(boolean sjRequired) {
        this.sjRequired = sjRequired;
    }

    public boolean isChsiRequired() {
        return chsiRequired;
    }

    public void setChsiRequired(boolean chsiRequired) {
        this.chsiRequired = chsiRequired;
    }

    public RsAccount getCreator() {
        return creator;
    }

    public void setCreator(RsAccount creator) {
        this.creator = creator;
    }

    public ChannelStatistic getStatistic() {
        return statistic;
    }

    public void setStatistic(ChannelStatistic statistic) {
        this.statistic = statistic;
    }
}
