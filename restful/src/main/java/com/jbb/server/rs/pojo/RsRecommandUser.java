package com.jbb.server.rs.pojo;

import com.jbb.server.common.Constants;
import com.jbb.server.core.domain.User;
import com.jbb.server.shared.rs.Util;

public class RsRecommandUser {
    private String username;
    private String sex;
    private Integer age;
    private String birthPlace;
    private String phoneNumber;
    private String sesameCreditScore;
    private String creationDate;
    private String wechat;
    private String sourceId;
    private String qq;
    private String targetUsers;
    
    
    private String reason;
    private Integer point;
    private String reasonDesc;

    public RsRecommandUser() {

    }

    public RsRecommandUser(User user) {
        this.username = user.getUserName();
        this.phoneNumber = user.getPhoneNumber();
        this.sex = user.getSex();
        this.age = user.getAge();
        this.birthPlace = user.getBirthPlace();
        this.wechat = user.getWechat();
        this.creationDate = Util.printDateTime(user.getCreationDate());
        this.sesameCreditScore = user.getPropertyVal(Constants.SESAME_CREDIT_SCORE);
        this.sourceId = user.getSourceId();
        this.qq= user.getQqNumber();
        this.reason = user.getReason();
        this.reasonDesc = user.getReasonDesc();
        this.point=user.getReasonPoint();
    }

    public RsRecommandUser(User user, boolean hidden) {
        this(user);
        if (hidden) {
            String phone = user.getPhoneNumber();
            if (phone != null && phone.length() == 11) {
                this.phoneNumber = phone.substring(0, 3) + "********";
            } else {
                phone = null;
            }
            if (this.username != null) {
                this.username = this.username.substring(0, 1) + "**";
            }
            this.sesameCreditScore = null;
            this.wechat = null;
            this.sourceId = null;
        }

    }
    
    public String getTargetUsers() {
        return targetUsers;
    }

    public void setTargetUsers(String targetUsers) {
        this.targetUsers = targetUsers;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSesameCreditScore() {
        return sesameCreditScore;
    }

    public void setSesameCreditScore(String sesameCreditScore) {
        this.sesameCreditScore = sesameCreditScore;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
    
    

}
