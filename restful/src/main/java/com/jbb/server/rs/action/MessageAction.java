package com.jbb.server.rs.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.core.domain.Message;
import com.jbb.server.core.service.MessageService;
import com.jbb.server.rs.pojo.ActionResponse;

@Service(MessageAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MessageAction extends BasicAction {

    private static Logger logger = LoggerFactory.getLogger(MessageAction.class);
    public static final String ACTION_NAME = "MessageAction";

    @Autowired
    private MessageService messageservice;

    private Response response;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    public void getMessages(Boolean read, Integer lastMessageId, Integer forward, int pageSize) {
        logger.debug(">getMessages() read = " + read + " lastMessageId = " + lastMessageId + " forward = " + forward
            + " pageSize = " + pageSize);
        List<Message> messages =
            messageservice.getMessages(null, this.user.getUserId(), read, lastMessageId, forward, pageSize);

        this.response.messages = messages;
        logger.debug("<getmessages()");
    }

    public void updateMessageStatusRead(int messageId, boolean read) {
        logger.debug(">updateMessageStatusRead() messageId = " + messageId + " read = " + read);
        messageservice.updateMessageStatusRead(messageId, this.user.getUserId(), read);
        logger.debug("<updateMessageStatusRead()");

    }

    public void updateMessageStatusHidden(int messageId, boolean hidden) {
        logger.debug(">updateMessageStatusHidden() messageId = " + messageId + " hidden = " + hidden);
        messageservice.updateMessageStatusHidden(messageId, this.user.getUserId(), hidden);
        logger.debug("<updateMessageStatusRead()");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private List<Message> messages;

        public List<Message> getMessages() {
            return messages;
        }

    }

}
