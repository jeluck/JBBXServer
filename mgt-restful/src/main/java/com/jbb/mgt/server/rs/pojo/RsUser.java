package com.jbb.mgt.server.rs.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.User;
import com.jbb.server.shared.rs.Util;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsUser {
    private int userId;
    private String userName;
    private String phoneNumber;// 电话号码
    private String idCard;// 身份证号码
    private String ipAddress;
    private Integer zhimaScore;
    private String qq;
    private String wechat;
    private String channelCode;
    private boolean realnameVerified;
    private boolean mobileVerified;
    private boolean zhimaVerified;
    private boolean jingdongVerified;
    private boolean siVerified;
    private boolean gjjVerified;
    private boolean chsiVerified;
    private Long creationDate;
    private String idcardRear;// 身份证正面
    private String idcardBack;// 身份证反面
    private String idcardInfo;// 手持身份证
    private String contract1Relation;// 联系人1关系
    private String contract1Username;// 联系人1名字
    private String contract1Phonenumber;// 联系人1联系方式
    private String contract2Relation;// 联系人2关系
    private String contract2Username;// 联系人2名字
    private String contract2Phonenumber;// 联系人2联系方式

    private String sex;
    private Integer age;
    private String census;// 户籍

    public RsUser() {}

    public RsUser(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.phoneNumber = user.getPhoneNumber();
        this.idCard = user.getIdCard();
        this.creationDate = Util.getTimeMs(user.getCreationDate());
        this.channelCode = user.getChannelCode();
        this.ipAddress = user.getIpAddress();
        this.zhimaScore = user.getZhimaScore();
        this.qq = user.getQq();
        this.wechat = user.getWechat();
        this.realnameVerified = user.isRealnameVerified();
        this.mobileVerified = user.isMobileVerified();
        this.zhimaVerified = user.isZhimaVerified();
        this.jingdongVerified = user.isJingdongVerified();
        this.chsiVerified = user.isChsiVerified();
        this.siVerified = user.isSiVerified();
        this.gjjVerified = user.isGjjVerified();
        this.idcardRear = user.getIdcardRear();
        this.idcardBack = user.getIdcardBack();
        this.idcardInfo = user.getIdcardInfo();
        this.contract1Username = user.getContract1Username();
        this.contract1Phonenumber = user.getContract1Phonenumber();
        this.contract1Relation = user.getContract1Relation();
        this.contract2Username = user.getContract2Username();
        this.contract2Phonenumber = user.getContract2Phonenumber();
        this.contract2Relation = user.getContract2Relation();

        this.sex = user.getSex();
        this.age = user.getAge();
        this.census = user.getCensus();
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getIdCard() {
        return idCard;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Integer getZhimaScore() {
        return zhimaScore;
    }

    public String getQq() {
        return qq;
    }

    public String getWechat() {
        return wechat;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public boolean isRealnameVerified() {
        return realnameVerified;
    }

    public boolean isMobileVerified() {
        return mobileVerified;
    }

    public boolean isZhimaVerified() {
        return zhimaVerified;
    }

    public boolean isJingdongVerified() {
        return jingdongVerified;
    }

    public boolean isSiVerified() {
        return siVerified;
    }

    public boolean isGjjVerified() {
        return gjjVerified;
    }

    public boolean isChsiVerified() {
        return chsiVerified;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public String getIdcardRear() {
        return idcardRear;
    }

    public String getIdcardBack() {
        return idcardBack;
    }

    public String getIdcardInfo() {
        return idcardInfo;
    }

    public String getContract1Relation() {
        return contract1Relation;
    }

    public String getContract1Username() {
        return contract1Username;
    }

    public String getContract1Phonenumber() {
        return contract1Phonenumber;
    }

    public String getContract2Relation() {
        return contract2Relation;
    }

    public String getContract2Username() {
        return contract2Username;
    }

    public String getContract2Phonenumber() {
        return contract2Phonenumber;
    }

    public String getSex() {
        return sex;
    }

    public Integer getAge() {
        return age;
    }

    public String getCensus() {
        return census;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setZhimaScore(Integer zhimaScore) {
        this.zhimaScore = zhimaScore;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public void setRealnameVerified(boolean realnameVerified) {
        this.realnameVerified = realnameVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public void setZhimaVerified(boolean zhimaVerified) {
        this.zhimaVerified = zhimaVerified;
    }

    public void setJingdongVerified(boolean jingdongVerified) {
        this.jingdongVerified = jingdongVerified;
    }

    public void setSiVerified(boolean siVerified) {
        this.siVerified = siVerified;
    }

    public void setGjjVerified(boolean gjjVerified) {
        this.gjjVerified = gjjVerified;
    }

    public void setChsiVerified(boolean chsiVerified) {
        this.chsiVerified = chsiVerified;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public void setIdcardRear(String idcardRear) {
        this.idcardRear = idcardRear;
    }

    public void setIdcardBack(String idcardBack) {
        this.idcardBack = idcardBack;
    }

    public void setIdcardInfo(String idcardInfo) {
        this.idcardInfo = idcardInfo;
    }

    public void setContract1Relation(String contract1Relation) {
        this.contract1Relation = contract1Relation;
    }

    public void setContract1Username(String contract1Username) {
        this.contract1Username = contract1Username;
    }

    public void setContract1Phonenumber(String contract1Phonenumber) {
        this.contract1Phonenumber = contract1Phonenumber;
    }

    public void setContract2Relation(String contract2Relation) {
        this.contract2Relation = contract2Relation;
    }

    public void setContract2Username(String contract2Username) {
        this.contract2Username = contract2Username;
    }

    public void setContract2Phonenumber(String contract2Phonenumber) {
        this.contract2Phonenumber = contract2Phonenumber;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setCensus(String census) {
        this.census = census;
    }
}
