package com.jbb.server.rs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.service.LeanCloudService;
import com.jbb.server.rs.pojo.ActionResponse;
import com.jbb.server.shared.rs.RsSignature;
import com.jbb.server.shared.rs.Util;

@Service(SignatureAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SignatureAction extends BasicAction {

    private static Logger logger = LoggerFactory.getLogger(SignatureAction.class);

    public static final String ACTION_NAME = "SignatureAction";

    private Response response;

    @Autowired
    private LeanCloudService leanCloudService;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    public void getSignature(String clientId, String sortedMemberIds, String convId, String action) {
        logger.debug(">getSignature()");

        // check require param
        Util.checkRequiredParam("clientId", clientId);
        long timestamp = DateUtil.getCurrentTimeSec();
        String nonce = StringUtil.randomAlphaNum(16);
        // 创建会话签名
        String signature = null;
        if (!StringUtil.isEmpty(sortedMemberIds)) {
            signature = leanCloudService.getConvSignature(clientId, sortedMemberIds, nonce, timestamp, convId, action);
        } else {
            // 登录签名
            signature = leanCloudService.getLoginSignature(clientId, nonce, timestamp);
        }
        this.response.signature = new RsSignature(signature, timestamp, nonce);
        logger.debug("<getSignature()");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {

        private RsSignature signature;

        public RsSignature getSignature() {
            return signature;
        }

    }

}
