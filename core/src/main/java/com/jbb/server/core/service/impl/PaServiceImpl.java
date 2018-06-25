package com.jbb.server.core.service.impl;

import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.HttpUtil;
import com.jbb.server.common.util.HttpUtil.HttpResponse;
import com.jbb.server.core.dao.UserEventLogDao;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.service.PaService;

@Service("paService")
public class PaServiceImpl implements PaService {

    private static Logger logger = LoggerFactory.getLogger(PaServiceImpl.class);

    @Autowired
    private UserEventLogDao userEventLogDao;

    @Override
    public void postUserToPa(User user, String remoteAddress) {
        logger.debug(">postUserData");

        String url = "http://121.196.206.97/insurance/appsvr/life/donate";

        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("mobile=");
            stringBuilder.append(URLEncoder.encode(user.getPhoneNumber(), "utf-8"));
            stringBuilder.append("&idNo=");
            stringBuilder.append(URLEncoder.encode(user.getIdCardNo(), "utf-8"));
            stringBuilder.append("&name=");
            stringBuilder.append(URLEncoder.encode(user.getUserName(), "utf-8"));
            stringBuilder.append("&channel=FA24");
            stringBuilder.append("&customerIp=");
            stringBuilder.append(URLEncoder.encode(remoteAddress, "utf-8"));
            String data  = stringBuilder.toString();
            logger.debug("url=" + url + ", date="+data);

            HttpResponse response = HttpUtil.sendDataViaHTTPRepeat(HttpUtil.HTTP_POST, url,
                HttpUtil.CONTENT_TYPE_X_WWW_FORM_URLENCODED, data, null, "paService");
            if (response.getResponseCode() == HttpUtil.STATUS_OK) {
                userEventLogDao.insertEventLog(user.getUserId(), null, "PA", "POST", "SUCCESS", new String(response.getResponseBody()), remoteAddress,
                    DateUtil.getCurrentTimeStamp());
                logger.debug("response = " + new String(response.getResponseBody()));
            } else {
                userEventLogDao.insertEventLog(user.getUserId(), null, "CY", "POST", "ERROR", String.valueOf(response.getResponseCode()), remoteAddress,
                    DateUtil.getCurrentTimeStamp());
                logger.debug("response code = " + response.getResponseCode());
            }

        } catch (Exception e) {
            logger.error("postUserData error", e);
        }
        logger.debug("<postUserData");

    }

}
