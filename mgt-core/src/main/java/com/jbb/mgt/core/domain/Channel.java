package com.jbb.mgt.core.domain;

import java.sql.Timestamp;

import com.jbb.server.common.Constants;

/**
 * 渠道信息实体类
 * 
 * @author wyq
 * @date 2018年4月19日 19:30:09
 *
 */
public class Channel {

    private String channelCode;
    private String channelName;
    private String channelUrl;
    private String serviceQQ;
    private String serviceWechat;
    private int status;
    private int mode;
    private String sourcePhoneNumber;
    private String sourcePassword;
    private int creator;
    private Timestamp creationDate;
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
    private Account account;
    private ChannelStatistic statistic;

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

    public String getSourcePassword() {
        return sourcePassword;
    }

    public void setSourcePassword(String sourcePassword) {
        this.sourcePassword = sourcePassword;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
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

    public int getReceiveMode() {
        return receiveMode;
    }

    public void setReceiveMode(int receiveMode) {
        this.receiveMode = receiveMode;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public ChannelStatistic getStatistic() {
        return statistic;
    }

    public void setStatistic(ChannelStatistic statistic) {
        this.statistic = statistic;
    }
    
    public boolean isJbbChannel(){
        if(this.account!=null && this.account.getOrgId() == Constants.JBB_ORG){
            return true;
        }
        return false;
    }

    public Channel() {
        super();
    }

    public Channel(String channelCode, String channelName, String channelUrl, String serviceQQ, String serviceWechat,
        int status, int mode, String sourcePhoneNumber, String sourcePassword, int creator, Timestamp creationDate,
        boolean qqRequired, boolean wechatRequired, boolean zhimaRequired, boolean idcardInfoRequired,
        boolean idcardBackRequired, boolean idcardRearRequired, boolean headerRequired, boolean mobileContract1Required,
        boolean mobileContract2Required, boolean mobileServiceInfoRequired, int cpaPrice, int cpsPrice, int receiveMode,
        boolean hidden, boolean taobaoRequired, boolean jingdongRequired, boolean gjjRequired, boolean sjRequired,
        boolean chsiRequired, Account account, ChannelStatistic statistic) {
        this.channelCode = channelCode;
        this.channelName = channelName;
        this.channelUrl = channelUrl;
        this.serviceQQ = serviceQQ;
        this.serviceWechat = serviceWechat;
        this.status = status;
        this.mode = mode;
        this.sourcePhoneNumber = sourcePhoneNumber;
        this.sourcePassword = sourcePassword;
        this.creator = creator;
        this.creationDate = creationDate;
        this.qqRequired = qqRequired;
        this.wechatRequired = wechatRequired;
        this.zhimaRequired = zhimaRequired;
        this.idcardInfoRequired = idcardInfoRequired;
        this.idcardBackRequired = idcardBackRequired;
        this.idcardRearRequired = idcardRearRequired;
        this.headerRequired = headerRequired;
        this.mobileContract1Required = mobileContract1Required;
        this.mobileContract2Required = mobileContract2Required;
        this.mobileServiceInfoRequired = mobileServiceInfoRequired;
        this.cpaPrice = cpaPrice;
        this.cpsPrice = cpsPrice;
        this.receiveMode = receiveMode;
        this.hidden = hidden;
        this.taobaoRequired = taobaoRequired;
        this.jingdongRequired = jingdongRequired;
        this.gjjRequired = gjjRequired;
        this.sjRequired = sjRequired;
        this.chsiRequired = chsiRequired;
        this.account = account;
        this.statistic = statistic;
    }
}
