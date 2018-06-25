package com.jbb.mgt.rs.action.msgCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbb.mgt.core.service.AliyunService;
import com.jbb.mgt.server.rs.action.BasicAction;

@Component(AfsCheckAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AfsCheckAction extends BasicAction {

    public static final String ACTION_NAME = "AfsCheckAction";

    private static Logger logger = LoggerFactory.getLogger(AfsCheckAction.class);

    @Autowired
    private AliyunService aliyunService;

    public void afsCheck(String sessionId, String sig, String token, String scene, String remoteIp) {
        logger.debug(">afsCheck()");
        aliyunService.afsCheck(sessionId, sig, token, scene, remoteIp);
        logger.debug("<afsCheck()");
    }

}
