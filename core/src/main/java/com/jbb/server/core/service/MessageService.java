 package com.jbb.server.core.service;

import java.util.List;

import com.jbb.server.core.domain.Message;

public interface MessageService {
     

     /**
      * 插入消息
      * @param message
      */
     void insertMessage(Message message);

     /**
      * 获取消息列表
      * 
      * @param fromUserId :发送方userId
      * @param toUserId:接收方userId
      * @param isUnread :是否已读
      * @param lastMessageId ：最后一条消息ID
      * @param forward : 1 加载历史 (messageId<lastMessageId) ， 0 刷新 (messageId>lastMessageId)
      * @param pageSize : 每次获取条数
      * @return
      */
     List<Message> getMessages(Integer fromUserId, int toUserId, Boolean isUnread, Integer lastMessageId, Integer forward,
         int pageSize);

     /**
      * 获取未读消息数
      * 
      * @param userId
      * @return
      */
     int getUnreadMessagesCount(int toUserId);

     /**
      * 通过消息ID获取消息
      * 
      * @param messageId
      * @return
      */
     Message getMessageById(int messageId);

     /**
      * 更新消息是否已读
      * 
      * @param messageId，若为null, 标示所有此用户的消息状态
      * @param userId
      * @param read
      */
     void updateMessageStatusRead(Integer messageId, int userId, boolean read);

     /**
      * 更新消息是否隐藏(删除)
      * 
      * @param messageId, 若为null, 标示所有此用户的消息状态
      * @param userId
      * @param hide
      */
     void updateMessageStatusHidden(Integer messageId, int userId, boolean hidden);
     


}
