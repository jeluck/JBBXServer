package com.jbb.server.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.server.core.dao.MessageDao;
import com.jbb.server.core.domain.Message;
import com.jbb.server.core.service.MessageService;

@Service("MessageService")
public class MessageServiceImpl implements MessageService {
    
    
    @Autowired
    private MessageDao messagedao;

    @Override
    public void insertMessage(Message message) {
        messagedao.insertMessage(message);
    }

    @Override
    public List<Message> getMessages(Integer fromUserId, int toUserId, Boolean isUnread, Integer lastMessageId,
        Integer forward, int pageSize) {
         return messagedao.selectMessages(fromUserId, toUserId, isUnread, lastMessageId, forward, pageSize);
    }

    @Override
    public int getUnreadMessagesCount(int toUserId) {
         return messagedao.countUnreadMessage(toUserId);
    }

    @Override
    public Message getMessageById(int messageId) {
         return messagedao.selectMessageById(messageId);
    }

    @Override
    public void updateMessageStatusRead(Integer messageId, int userId, boolean read) {
        messagedao.updateMessageStatusRead(messageId, userId, read);
         
    }

    @Override
    public void updateMessageStatusHidden(Integer messageId, int userId, boolean hidden) {
        messagedao.updateMessageStatusHidden(messageId, userId, hidden);
    }

}
