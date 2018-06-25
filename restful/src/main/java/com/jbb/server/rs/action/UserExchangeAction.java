package com.jbb.server.rs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbb.server.core.service.LeanCloudService;
import com.jbb.server.shared.rs.Util;

@Component(UserExchangeAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserExchangeAction extends BasicAction {
    
    private static Logger logger = LoggerFactory.getLogger(UserExchangeAction.class);
    public static final String ACTION_NAME = "UserExchangeAction";

    @Autowired
    private LeanCloudService LeanCloudService;

    /**
     * 交换信息接口
     * 
     * @param toUserId
     * @param exchangeType
     * @param convId
     */
    public void exchangeInfo(Integer toUserId, Integer exchangeType, String convId, 
        String oldMsgId, Long oldMsgTs) {
        Util.checkRequiredParam("toUserId", toUserId);
        Util.checkRequiredParam("exchangeType", exchangeType);
        Util.checkRequiredParam("convId", convId);

        LeanCloudService.exchangeInfo(this.user.getUserId(), toUserId, exchangeType, convId, oldMsgId, oldMsgTs);

    }

}
