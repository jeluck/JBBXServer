package com.jbb.server.core.domain;

public class Authorization {
    private String key;
    private boolean qqPriv;
    private boolean wechatPriv;
    private boolean phonePriv;
    private boolean infoPriv;
    
    public Authorization() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isQqPriv() {
        return qqPriv;
    }

    public void setQqPriv(boolean qqPriv) {
        this.qqPriv = qqPriv;
    }

    public boolean isWechatPriv() {
        return wechatPriv;
    }

    public void setWechatPriv(boolean wechatPriv) {
        this.wechatPriv = wechatPriv;
    }

    public boolean isPhonePriv() {
        return phonePriv;
    }

    public void setPhonePriv(boolean phonePriv) {
        this.phonePriv = phonePriv;
    }

    public boolean isInfoPriv() {
        return infoPriv;
    }

    public void setInfoPriv(boolean infoPriv) {
        this.infoPriv = infoPriv;
    }

    @Override
    public String toString() {
        return "Authorization [key=" + key + ", qqPriv=" + qqPriv + ", wechatPriv=" + wechatPriv + ", phonePriv="
            + phonePriv + ", infoPriv=" + infoPriv + "]";
    }
}
