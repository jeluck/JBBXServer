package com.jbb.server.rs.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.jbb.server.core.service.WeixinPayService;

@Service(WxPayNotifyAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WxPayNotifyAction extends BasicAction {

    private static Logger logger = LoggerFactory.getLogger(WxPayNotifyAction.class);

    public static final String ACTION_NAME = "WxPayNotifyAction";

    @Autowired
    private WeixinPayService weixinPayService;

    public String payNotify(HttpServletRequest request) {
        logger.info(">payNotify, request=" + request);
        String xmlResult;
        try {
            xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            String result = weixinPayService.notifyPayResult(xmlResult);
            logger.info("<payNotify");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("<payNotify with error");
            return WxPayNotifyResponse.fail(e.getMessage());
        }

    }

}
