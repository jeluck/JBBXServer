package com.jbb.server.core.doman.notification;

import com.jbb.server.core.domain.Iou;
import com.jbb.server.core.domain.Message;

public class NotificationRequest {

    private int userId;

    private Iou iou;

    private Message message;

    public NotificationRequest() {}
    
    public NotificationRequest(Message message) {
        super();
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Iou getIou() {
        return iou;
    }

    public void setIou(Iou iou) {
        this.iou = iou;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "NotificationRequest [userId=" + userId + ", iou=" + iou + ", message=" + message + "]";
    }

}
