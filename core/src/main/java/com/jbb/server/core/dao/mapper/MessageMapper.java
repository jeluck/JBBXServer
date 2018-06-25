 package com.jbb.server.core.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.Message;

public interface MessageMapper {
     

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
     List<Message> selectMessages(@Param("fromUserId") Integer fromUserId, @Param("toUserId") int toUserId, @Param("isUnread") Boolean isUnread,@Param("lastMessageId") int lastMessageId,@Param("forward") Integer forward,
         @Param("pageSize") int pageSize);

     /**
      * 获取未读消息数
      * 
      * @param userId
      * @return
      */
     int countUnreadMessage(@Param("toUserId") int toUserId);

     /**
      * 通过消息ID获取消息
      * 
      * @param messageId
      * @return
      */
     Message selectMessageById(@Param("messageId") int messageId);

     /**
      * 更新消息是否已读
      * 
      * @param messageId，若为null, 标示所有此用户的消息状态
      * @param userId
      * @param read
      */
     void updateMessageStatusRead(@Param("messageId") Integer messageId,@Param("userId")  int userId, @Param("read") boolean read);

     /**
      * 更新消息是否隐藏(删除)
      * 
      * @param messageId, 若为null, 标示所有此用户的消息状态
      * @param userId
      * @param hide
      */
     void updateMessageStatusHidden(@Param("messageId") Integer messageId,@Param("userId")  int userId,@Param("hidden")  boolean hidden);
     
     

}
