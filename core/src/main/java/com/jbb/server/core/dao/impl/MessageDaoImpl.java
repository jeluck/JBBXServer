package com.jbb.server.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.server.core.dao.MessageDao;
import com.jbb.server.core.dao.mapper.MessageMapper;
import com.jbb.server.core.domain.Message;

@Repository("MessageDao")
public class MessageDaoImpl implements MessageDao {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void insertMessage(Message message) {
        messageMapper.insertMessage(message);
    }

    @Override
    public List<Message> selectMessages(Integer fromUserId, int toUserId, Boolean isUnread, Integer lastMessageId,
        Integer forward, int pageSize) {
        return messageMapper.selectMessages(fromUserId, toUserId, isUnread, lastMessageId, forward, pageSize);
    }

    @Override
    public int countUnreadMessage(int toUserId) {
        return messageMapper.countUnreadMessage(toUserId);
    }

    @Override
    public Message selectMessageById(int messageId) {
        return messageMapper.selectMessageById(messageId);
    }

    @Override
    public void updateMessageStatusRead(Integer messageId, int userId, boolean read) {
        messageMapper.updateMessageStatusRead(messageId, userId, read);
    }

    @Override
    public void updateMessageStatusHidden(Integer messageId, int userId, boolean hidden) {
        messageMapper.updateMessageStatusHidden(messageId, userId, hidden);
    }


}
