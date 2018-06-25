package com.jbb.mgt.proc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.mgt.core.domain.mapper.Mapper;
import com.jbb.mgt.core.domain.mq.RegisterEvent;
import com.jbb.mgt.core.service.RegisterEventService;
import com.jbb.mgt.server.core.util.SpringUtil;
import com.jbb.server.mq.MessageListener;

public class UserRegisterH5Listener implements MessageListener {

	private static Logger logger = LoggerFactory.getLogger(UserRegisterH5Listener.class);

	private RegisterEventService registerEventService;

	public UserRegisterH5Listener() {
		registerEventService = SpringUtil.getBean("RegisterEventService", RegisterEventService.class);
	}

	@Override
	public void onMessage(byte[] messageBody) {

		RegisterEvent event = Mapper.readRegisterEvent(messageBody);
		logger.debug(">onMessage, event = " + event);

		if (event == null) {
			logger.debug("parse event error, event is null = " + new String(messageBody));
			return;
		}
		registerEventService.processEvent(event);
		logger.debug("<onMessage");
	}

}
