package com.jbb.server.core.domain;

import java.sql.Timestamp;

import com.jbb.server.common.util.DateUtil;

public class Message {

    public static int TYPE_IOU_MESSAGE_NOTIFY = 1;
    public static int TYPE_IOU_MESSAGE_APPROVE = 2;
    public static int TYPE_IOU_MESSAGE_REJECT = 3;
    public static int TYPE_IOU_MESSAGE_ALERT = 4;

    private int messageId;

    /**
     * 发送方ID， 0则为系统发送
     */
    private int fromUserId;

    /**
     * 接收方ID
     */
    private int toUserId;

    private Timestamp creationDate;

    private Timestamp sendDate;

    private String subject;

    private int messageType;

    private String messageText;

    private boolean sms;

    private boolean push;

    private String parameters;

    private boolean read;

    private boolean hidden;

    public Message() {
        super();
    }

    public Message(int toUserId, String subject, int messageType, String messageText, boolean sms, boolean push,
        String parameters, Timestamp sendDate) {
        super();
        this.toUserId = toUserId;
        this.sendDate = sendDate;
        this.subject = subject;
        this.messageType = messageType;
        this.messageText = messageText;
        this.sms = sms;
        this.push = push;
        this.parameters = parameters;
        this.creationDate = DateUtil.getCurrentTimeStamp();
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getSendDate() {
        return sendDate;
    }

    public void setSendDate(Timestamp sendDate) {
        this.sendDate = sendDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public boolean isSms() {
        return sms;
    }

    public void setSms(boolean sms) {
        this.sms = sms;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public String toString() {
        return "Message [messageId=" + messageId + ", fromUserId=" + fromUserId + ", toUserId=" + toUserId
            + ", creationDate=" + creationDate + ", sendDate=" + sendDate + ", subject=" + subject + ", messageType="
            + messageType + ", messageText=" + messageText + ", sms=" + sms + ", push=" + push + ", parameters="
            + parameters + ", read=" + read + ", hidden=" + hidden + "]";
    }

}
