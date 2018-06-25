package com.jbb.server.core.domain;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.shared.rs.Util;

/**
 * 
 * @author VincentTang
 * @date 2017年12月21日
 */
@JsonInclude(JsonInclude.Include.NON_NULL)

public class User {
    private int userId;
    @JsonIgnore
    private UserKey userKey;

    private String userName;
    private String phoneNumber;
    private String email;
    private String idCardNo;
    private boolean creditCard;
    private int identityType;
    @JsonIgnore
    private String phoneServicePassword;
    private String bankName;
    private String bankCardNo;
    private int creditNumber;
    private boolean verified;
    private String avatarPic;
    private String nickName;
    @JsonIgnore
    private Timestamp creationDate;
    @JsonProperty("creationDate")
    private String creationDateStr;
    private Long creationDateMs;
    @JsonIgnore
    private String password;
    private String sex;
    private int age;
    private String nation;
    private String idcardAddress;
    private String wechat;
    private String qqNumber;
    private int addressBookNumber;
    private String phoneAuthenticationTime;
    private boolean married;
    private String liveAddress;
    private String parentAddress;
    private String education;
    private String occupation;
    // role and permissions
    private Integer roleId;
    private int[] permissions;
    private String birthPlace;
    @JsonIgnore
    private String sourceId;
    @JsonIgnore
    private String platform;

    private List<UserProperty> properites;

    private List<UserVerifyResult> verifyResults;

    // for lender reason
    private String reason;
    private String reasonDesc;
    private Integer reasonPoint;

    public User() {
        super();
    }

    public User(int userId, String nickname, String avatarPic, boolean verified) {
        super();
        this.userId = userId;
        this.nickName = nickname;
        this.avatarPic = avatarPic;
        this.verified = verified;
    }

    public User(String phoneNumber, String password, String nickName, long creationDateMs) {
        super();
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.nickName = nickName;
        this.creationDate = new Timestamp(creationDateMs);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserKey getUserKey() {
        return userKey;
    }

    public void setUserKey(UserKey userKey) {
        this.userKey = userKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public boolean isCreditCard() {
        return creditCard;
    }

    public void setCreditCard(boolean creditCard) {
        this.creditCard = creditCard;
    }

    public int getIdentityType() {
        return identityType;
    }

    public void setIdentityType(int identityType) {
        this.identityType = identityType;
    }

    public String getPhoneServicePassword() {
        return phoneServicePassword;
    }

    public void setPhoneServicePassword(String phoneServicePassword) {
        this.phoneServicePassword = phoneServicePassword;
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

    public int getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(int creditNumber) {
        this.creditNumber = creditNumber;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getAvatarPic() {
        return avatarPic;
    }

    public void setAvatarPic(String avatarPic) {
        this.avatarPic = avatarPic;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
        this.creationDateStr = Util.printDateTime(creationDate);
        this.creationDateMs = Util.getTimeMs(creationDate);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getAddressBookNumber() {
        return addressBookNumber;
    }

    public void setAddressBookNumber(int addressBookNumber) {
        this.addressBookNumber = addressBookNumber;
    }

    public String getPhoneAuthenticationTime() {
        return phoneAuthenticationTime;
    }

    public void setPhoneAuthenticationTime(String phoneAuthenticationTime) {
        this.phoneAuthenticationTime = phoneAuthenticationTime;
    }

    public boolean isMarried() {
        return married;
    }

    public void setMarried(boolean married) {
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

    public String getPropertyVal(String name) {
        if (CommonUtil.isNullOrEmpty(properites)) {
            return null;
        }
        for (UserProperty p : properites) {
            if (p.getName().equals(name)) {
                return p.getValue();
            }
        }
        return null;
    }

    public int getPropertyIntVal(String name, int defaultVal) {
        if (CommonUtil.isNullOrEmpty(properites)) {
            return defaultVal;
        }
        try {
            for (UserProperty p : properites) {
                if (p.getName().equals(name)) {
                    return Integer.valueOf(p.getValue());
                }
            }
        } catch (NumberFormatException e) {
            // nothing todo
        }
        return defaultVal;
    }

    public void setProperites(List<UserProperty> properites) {
        this.properites = properites;
    }

    public String getCreationDateStr() {
        return creationDateStr;
    }

    public void setCreationDateStr(String creationDateStr) {
        this.creationDateStr = creationDateStr;
    }

    public Long getCreationDateMs() {
        return creationDateMs;
    }

    public void setCreationDateMs(Long creationDateMs) {
        this.creationDateMs = creationDateMs;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public int[] getPermissions() {
        return permissions;
    }

    public void setPermissions(int[] permissions) {
        this.permissions = permissions;
    }

    public boolean checkPermission(int permission) {
        if (permissions == null)
            return false;
        for (int p : permissions) {
            if (p == permission)
                return true;
        }
        return false;
    }

    public boolean checkAccessAllPermission() {
        return checkPermission(Role.PERMISSION_ACCESS_ALL);
    }

    public boolean canGrantRolePermission() {
        return checkPermission(Role.PERMISSION_GRANT_ROLE);
    }

    public boolean canGetRecommandUsers() {
        return checkPermission(Role.PERMISSION_GET_RECOMMAND_USERS);
    }

    public boolean canStatisticUsers() {
        return checkPermission(Role.PERMISSION_STATISTIC_USERS);
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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

    public List<UserVerifyResult> getVerifyResults() {
        return verifyResults;
    }

    public void setVerifyResults(List<UserVerifyResult> verifyResults) {
        this.verifyResults = verifyResults;
    }

    public Integer getReasonPoint() {
        return reasonPoint;
    }

    public void setReasonPoint(Integer reasonPoint) {
        this.reasonPoint = reasonPoint;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", userName=" + userName + ", phoneNumber=" + phoneNumber + ", email=" + email
            + ", idCardNo=" + idCardNo + ", creditCard=" + creditCard + ", identityType=" + identityType
            + ", phoneServicePassword=" + phoneServicePassword + ", bankName=" + bankName + ", bankCardNo=" + bankCardNo
            + ", creditNumber=" + creditNumber + ", verified=" + verified + ", avatarPic=" + avatarPic + ", nickName="
            + nickName + ", creationDate=" + creationDate + ", creationDateStr=" + creationDateStr + ", creationDateMs="
            + creationDateMs + ", password=" + password + ", sex=" + sex + ", age=" + age + ", nation=" + nation
            + ", idcardAddress=" + idcardAddress + ", wechat=" + wechat + ", qqNumber=" + qqNumber
            + ", addressBookNumber=" + addressBookNumber + ", phoneAuthenticationTime=" + phoneAuthenticationTime
            + ", married=" + married + ", liveAddress=" + liveAddress + ", parentAddress=" + parentAddress
            + ", education=" + education + ", occupation=" + occupation + ", roleId=" + roleId + ", permissions="
            + Arrays.toString(permissions) + ", birthPlace=" + birthPlace + ", sourceId=" + sourceId + ", platform="
            + platform + ", properites=" + properites + ", verifyResults=" + verifyResults + ", reason=" + reason
            + ", reasonDesc=" + reasonDesc + "]";
    }

}
