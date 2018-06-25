package com.jbb.server.rs.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.core.domain.IntendRecord;
import com.jbb.server.core.domain.Iou;
import com.jbb.server.core.domain.IouIntention;
import com.jbb.server.core.domain.UserBasic;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsIou {
    // 借条信息
    private Iou iou;
    // 出借人统计数
    private int intentionalUserCnt;
    // 出借人意向表
    private List<IntendRecord> intentionalUsers;
    // 借款人基本信息
    private UserBasic borrower;
    // 出借人基本信息
    private UserBasic lender;
    //是否已经存在出借意向
    private Boolean intention;
    //是否已经关注
    private Boolean followed;
    
    //调用人的出借状态
    private IouIntention intentionInfo;
    
    public RsIou() {
        super();
    }

    public RsIou(Iou iou) {
        super();
        this.iou = iou;
        this.iou.prepareCalFields();
    }
    
    public RsIou(Iou iou, boolean returnPrivacy) {
        super();
        this.iou = iou; 
        this.iou.prepareCalFields();
        if(!returnPrivacy){
            this.iou.setLenderId(-1);
            this.iou.setEffectiveDateStr(null);
            this.iou.setEffectiveDateTs(null);
            this.iou.setLenderDeleted(false);
            this.iou.setLastUpdateStatusDateStr(null);
            this.iou.setLastUpdateStatusDateTs(null);
        }
    }

    public Iou getIou() {
        return iou;
    }

    public void setIou(Iou iou) {
        this.iou = iou;
    }

    public int getIntentionalUserCnt() {
        return intentionalUserCnt;
    }

    public void setIntentionalUserCnt(int intentionalUserCnt) {
        this.intentionalUserCnt = intentionalUserCnt;
    }

    public List<IntendRecord> getIntentionalUsers() {
        return intentionalUsers;
    }

    public void setIntentionalUsers(List<IntendRecord> intentionalUsers) {
        this.intentionalUsers = intentionalUsers;
    }

    public UserBasic getBorrower() {
        return borrower;
    }

    public void setBorrower(UserBasic borrower) {
        this.borrower = borrower;
    }

    public UserBasic getLender() {
        return lender;
    }

    public void setLender(UserBasic lender) {
        this.lender = lender;
    }

    public Boolean getIntention() {
        return intention;
    }

    public void setIntention(Boolean intention) {
        this.intention = intention;
    }

    public Boolean getFollowed() {
        return followed;
    }

    public void setFollowed(Boolean followed) {
        this.followed = followed;
    }

    public IouIntention getIntentionInfo() {
        return intentionInfo;
    }

    public void setIntentionInfo(IouIntention intentionInfo) {
        this.intentionInfo = intentionInfo;
    }
    
    
    
}
