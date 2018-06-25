package com.jbb.server.rs.pojo.request;

import java.util.List;

import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserProperty;

public class ReUser {
    private String userName;
    private String email;
    private String idCardNo;
    private Boolean creditCard;
    private int creditNumber;
    private String bankName;
    private String bankCardNo;
    private String nickName;
    private String sex;
    private Integer age;
    private String nation;
    private String idcardAddress;
    private String phoneNumber;
    private String wechat;
    private String qqNumber;
    private Integer addressBookNumber;
    private String phoneAuthenticationTime;
    private Boolean married;
    private String liveAddress;
    private String parentAddress;
    private String education;
    private String occupation;

    private List<UserProperty> properites;
    
    public ReUser() {
    }

    
    
    public ReUser(User user,List<UserProperty> properites) {
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.idCardNo = user.getIdCardNo();
        this.creditNumber = user.getCreditNumber();
        this.bankName = user.getBankName();
        this.bankCardNo = user.getBankCardNo();
        this.nickName = user.getNickName();
        this.sex = user.getSex();
        this.age = user.getAge();
        this.nation = user.getNation();
        this.idcardAddress = user.getIdcardAddress();
        this.phoneNumber = user.getPhoneNumber();
        this.wechat = user.getWechat();
        this.qqNumber = user.getQqNumber();
        this.addressBookNumber = user.getAddressBookNumber();
        this.phoneAuthenticationTime = user.getPhoneAuthenticationTime();
        this.married = user.isMarried();
        this.liveAddress = user.getLiveAddress();
        this.parentAddress = user.getParentAddress();
        this.education = user.getEducation();
        this.occupation = user.getOccupation();
        this.properites = properites;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public Boolean getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(Boolean creditCard) {
        this.creditCard = creditCard;
    }
    
    public int getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(int creditNumber) {
        this.creditNumber = creditNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    
    
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getIdcardAddress() {
        return idcardAddress;
    }

    public void setIdcardAddress(String idcardAddress) {
        this.idcardAddress = idcardAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getQqNumber() {
        return qqNumber;
    }

    public void setQqNumber(String qqNumber) {
        this.qqNumber = qqNumber;
    }

    public Integer getAddressBookNumber() {
        return addressBookNumber;
    }

    public void setAddressBookNumber(Integer addressBookNumber) {
        this.addressBookNumber = addressBookNumber;
    }

    public String getPhoneAuthenticationTime() {
        return phoneAuthenticationTime;
    }

    public void setPhoneAuthenticationTime(String phoneAuthenticationTime) {
        this.phoneAuthenticationTime = phoneAuthenticationTime;
    }

    public Boolean getMarried() {
        return married;
    }

    public void setMarried(Boolean married) {
        this.married = married;
    }

    public String getLiveAddress() {
        return liveAddress;
    }

    public void setLiveAddress(String liveAddress) {
        this.liveAddress = liveAddress;
    }

    public String getParentAddress() {
        return parentAddress;
    }

    public void setParentAddress(String parentAddress) {
        this.parentAddress = parentAddress;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public List<UserProperty> getProperites() {
        return properites;
    }

    public void setProperites(List<UserProperty> properites) {
        this.properites = properites;
    }

    @Override
    public String toString() {
        return "ReUser [userName=" + userName + ", email=" + email + ", idCardNo=" + idCardNo + ", creditCard="
            + creditCard + ", bankName=" + bankName + ", bankCardNo=" + bankCardNo + ", nickName=" + nickName + ", sex="
            + sex + ", age=" + age + ", nation=" + nation + ", idcardAddress=" + idcardAddress + ", phoneNumber=" + phoneNumber 
            + ", wechat=" + wechat + ", qqNumber=" + qqNumber + ", addressBookNumber=" + addressBookNumber + ", phoneAuthenticationTime="
            + phoneAuthenticationTime + ", married=" + married + ", liveAddress=" + liveAddress + ", parentAddress="
            + parentAddress + ", education=" + education + ", occupation=" + occupation + ", properites=" + properites
            + "]";
    }

    

}
