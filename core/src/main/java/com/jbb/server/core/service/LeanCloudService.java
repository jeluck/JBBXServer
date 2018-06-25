package com.jbb.server.core.service;

import com.jbb.server.core.domain.LcTextMessage;
import com.jbb.server.core.doman.notification.NotificationRequest;

public interface LeanCloudService {
    void sendMessage(LcTextMessage lcMessage);

    public String getConvSignature(String clientId, String sortedMemberIds, String nonce, long timestamp, String convId, String action);

    String getLoginSignature(String clientId, String nonce, long timestamp);
    
    void exchangeInfo(int from, int to, int cmdType, String convId, String oldMsgId, Long oldMsgTs);
    
    void pushNotification(NotificationRequest notificationReq);

}
