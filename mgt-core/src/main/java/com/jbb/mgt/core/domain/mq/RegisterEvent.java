package com.jbb.mgt.core.domain.mq;

public class RegisterEvent {

    private int step;
    private int userId;
    private String channelCode;

    public RegisterEvent() {
        super();
    }

    public RegisterEvent(int step, int userId, String channelCode) {
        super();
        this.step = step;
        this.userId = userId;
        this.channelCode = channelCode;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    @Override
    public String toString() {
        return "RegisterEvent [step=" + step + ", userId=" + userId + ", channelCode=" + channelCode + "]";
    }

}
