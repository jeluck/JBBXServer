package com.jbb.server.rs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbb.server.core.service.AliyunService;

@Component(MsgCodeAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MsgCodeAction extends BasicAction {
    private static Logger logger = LoggerFactory.getLogger(MsgCodeAction.class);
    public static final String ACTION_NAME = "msgCodeAction";

    @Autowired
    private AliyunService aliyunService;

    public void sendCode(String phoneNumber) {
        logger.debug(">sendMsgCode()");

        logger.debug(">sencCode() phoneNumber=" + phoneNumber);
        aliyunService.sendCode(phoneNumber);
        logger.debug("<sencCode()");

        logger.debug("<sendMsgCode()");
    }

}
