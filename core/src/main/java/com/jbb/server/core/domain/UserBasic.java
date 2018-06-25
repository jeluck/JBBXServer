package com.jbb.server.core.domain;

public class UserBasic {
    private int userId;
    private String avatarPic;
    private String nickName;
    private boolean verified;
    private String username;

    // 意向出借记录
    private IntendRecord intendRecord;

    public UserBasic() {}

    public UserBasic(User user) {
        this.userId = user.getUserId();
        this.avatarPic = user.getAvatarPic();
        this.nickName = user.getNickName();
        this.verified = user.isVerified();
    }

    public UserBasic(User user, boolean returnPrivacy) {
        this.userId = user.getUserId();
        this.avatarPic = user.getAvatarPic();
        this.nickName = user.getNickName();
        this.verified = user.isVerified();
        if (returnPrivacy) {
            this.username = user.getUserName();
        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public IntendRecord getIntendRecord() {
        return intendRecord;
    }

    public void setIntendRecord(IntendRecord intendRecord) {
        this.intendRecord = intendRecord;
    }
    
    

}
