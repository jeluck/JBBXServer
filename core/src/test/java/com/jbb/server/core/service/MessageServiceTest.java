package com.jbb.server.core.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class MessageServiceTest {
    
    @Autowired
    private MessageService messageService;
    
    @Test
    @Transient
    public void insertMessage() {
        Message message = new Message();
        message.setCreationDate(DateUtil.getCurrentTimeStamp());
        message.setFromUserId(0);
        message.setHidden(false);
        message.setMessageText("这是测试消息10");
        message.setMessageType(0);
        message.setParameters("测试参数");
        message.setPush(false);
        message.setRead(false);
        message.setSendDate(DateUtil.getCurrentTimeStamp());
        message.setSms(false);
        message.setSubject("测试消息");
        message.setToUserId(1000000);
        messageService.insertMessage(message);
    }
    
    @Test
    @Transient
    public void getMessage() {
        Message message =  messageService.getMessageById(1);
        System.out.println("message "+message.getMessageText());
    }
    
    @Test
    @Transient
    public void getUnreadMessagesCount() {
        int  messagenum =  messageService.getUnreadMessagesCount(1);
        System.out.println("messagenum "+messagenum);
    }
    
    @Test
    @Transient
    public void updateMessageStatusRead() {
        messageService.updateMessageStatusRead(null, 1, true);
    }
    
    @Test
    @Transient
    public void updateMessageStatusHidden() {
        messageService.updateMessageStatusHidden(1, 1, false);
    }
    
    @Test
    @Transient
    public void getMessages() {
        List<Message> messagelist = messageService.getMessages(0, 1, false, 4, 1, 5);
    }
    
    

}
