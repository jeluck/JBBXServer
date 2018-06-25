package com.jbb.server.core.domain;

import java.util.Date;

public class Users {
    private Integer userId;

    private String userName;

    private String phoneNumber;

    private String email;

    private String idcardNo;

    private String isCreditCard;

    private Byte identityType;

    private String phoneServicePassword;

    private String bankName;

    private String bankCardNo;

    private Integer creditNumber;

    private Byte isVerified;

    private String avatarPic;

    private String nickName;

    private String password;

    private Date creationDate;
    
    public Users() {
		super();
	}

	public Users(String phoneNumber, Byte isVerified, String nickName, String password, Date creationDate) {
		super();
		this.phoneNumber = phoneNumber;
		this.isVerified = isVerified;
		this.nickName = nickName;
		this.password = password;
		this.creationDate = creationDate;
	}

	public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo == null ? null : idcardNo.trim();
    }

    public String getIsCreditCard() {
        return isCreditCard;
    }

    public void setIsCreditCard(String isCreditCard) {
        this.isCreditCard = isCreditCard == null ? null : isCreditCard.trim();
    }

    public Byte getIdentityType() {
        return identityType;
    }

    public void setIdentityType(Byte identityType) {
        this.identityType = identityType;
    }

    public String getPhoneServicePassword() {
        return phoneServicePassword;
    }

    public void setPhoneServicePassword(String phoneServicePassword) {
        this.phoneServicePassword = phoneServicePassword == null ? null : phoneServicePassword.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo == null ? null : bankCardNo.trim();
    }

    public Integer getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(Integer creditNumber) {
        this.creditNumber = creditNumber;
    }

    public Byte getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Byte isVerified) {
        this.isVerified = isVerified;
    }

    public String getAvatarPic() {
        return avatarPic;
    }

    public void setAvatarPic(String avatarPic) {
        this.avatarPic = avatarPic == null ? null : avatarPic.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}