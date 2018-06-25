package com.jbb.mgt.server.rs.pojo;

import com.jbb.mgt.core.domain.UserApplyRecord;
import com.jbb.server.shared.rs.Util;

/**
 * userApplyRecord response类
 *
 * @author wyq
 * @date 2018/6/9 19:25
 */
public class RsUserApplyRecord {
    /** 申请ID */
    private Integer applyId;
    private Integer userId;
    private Integer orgId;
    private Integer status;
    private Long assingDate;
    private RsUser user;
    private RsUserApplyRecordChannel channel;

    public RsUserApplyRecord() {
        super();
    }

    public RsUserApplyRecord(UserApplyRecord userApplyRecord) {
        this.applyId = userApplyRecord.getApplyId();
        this.userId = userApplyRecord.getUserId();
        this.orgId = userApplyRecord.getOrgId();
        this.status = userApplyRecord.getStatus();
        this.assingDate = Util.getTimeMs(userApplyRecord.getAssingDate());
        if (null != userApplyRecord.getUser()) {
            this.user = new RsUser();
            this.user.setUserId(userApplyRecord.getUser().getUserId());
            this.user.setUserName(userApplyRecord.getUser().getUserName());
            this.user.setPhoneNumber(userApplyRecord.getUser().getPhoneNumber());
            this.user.setIdCard(userApplyRecord.getUser().getIdCard());
            this.user.setIpAddress(userApplyRecord.getUser().getIpAddress());
            this.user.setZhimaScore(userApplyRecord.getUser().getZhimaScore());
            this.user.setRealnameVerified(userApplyRecord.getUser().isRealnameVerified());
            this.user.setMobileVerified(userApplyRecord.getUser().isMobileVerified());
            this.user.setZhimaVerified(userApplyRecord.getUser().isZhimaVerified());
            this.user.setJingdongVerified(userApplyRecord.getUser().isJingdongVerified());
            this.user.setSiVerified(userApplyRecord.getUser().isSiVerified());
            this.user.setGjjVerified(userApplyRecord.getUser().isGjjVerified());
            this.user.setChsiVerified(userApplyRecord.getUser().isChsiVerified());
        }
        if (null != userApplyRecord.getChannel()) {
            this.channel = new RsUserApplyRecordChannel();
            this.channel.setChannelCode(userApplyRecord.getChannel().getChannelCode());
            this.channel.setChannelName(userApplyRecord.getChannel().getChannelName());
        }
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getAssingDate() {
        return assingDate;
    }

    public void setAssingDate(Long assingDate) {
        this.assingDate = assingDate;
    }

    public RsUser getUser() {
        return user;
    }

    public void setUser(RsUser user) {
        this.user = user;
    }

    public RsUserApplyRecordChannel getChannel() {
        return channel;
    }

    public void setChannel(RsUserApplyRecordChannel channel) {
        this.channel = channel;
    }
}
