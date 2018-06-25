package com.jbb.server.registerproc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.core.service.UserRegisterProcessService;
import com.jbb.server.core.util.SpringUtil;
import com.jbb.server.mq.MessageListener;

public class UserRegisterH5Listener implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(UserRegisterH5Listener.class);

    private UserRegisterProcessService processor;

    public UserRegisterH5Listener() {
        processor = SpringUtil.getBean("userRegisterProcessService2", UserRegisterProcessService.class);
    }

    @Override
    public void onMessage(byte[] messageBody) {

        String message = new String(messageBody);

        int userId = Integer.valueOf(message);

        if (userId == 0) {
            logger.debug("not found user, userId = " + message);
            return;
        }
        processor.applyToLendUser(userId);

    }

}
