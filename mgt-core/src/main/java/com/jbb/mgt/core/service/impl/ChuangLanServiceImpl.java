package com.jbb.mgt.core.service.impl;

import java.io.IOException;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbb.mgt.core.dao.MessageCodeDao;
import com.jbb.mgt.core.domain.ChuangLanPhoneNumberRsp;
import com.jbb.mgt.core.domain.ChuangLanWoolCheckRsp;
import com.jbb.mgt.core.domain.SmsSendRequest;
import com.jbb.mgt.core.domain.SmsSendResponse;
import com.jbb.mgt.core.service.ChuangLanService;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.ChuangLanException;
import com.jbb.server.common.util.ChuangLanSmsUtil;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.HttpUtil;
import com.jbb.server.common.util.HttpUtil.HttpResponse;
import com.jbb.server.common.util.StringUtil;
import com.alibaba.fastjson.JSON;

@Service("ChuangLanService")
public class ChuangLanServiceImpl implements ChuangLanService {

    private static Logger logger = LoggerFactory.getLogger(ChuangLanServiceImpl.class);

    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MessageCodeDao messageCodeDao;
    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public ChuangLanWoolCheckRsp woolCheck(String mobile, String ipAddress) {
        String appId = PropertyManager.getProperty("jbb.mgt.woolcheck.api.appId");
        String appKey = PropertyManager.getProperty("jbb.mgt.woolcheck.api.appKey");
        String url = PropertyManager.getProperty("jbb.mgt.woolcheck.api.url");
        StringBuffer sb = new StringBuffer();
        String mimeBoundary = "--" + HttpUtil.CONTENT_TYPE_FORM_DATA_BOUNDARY;
        // appId
        sb = sb.append("--").append(mimeBoundary);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"appId\"");
        sb.append("\r\n\r\n");
        sb.append(appId);
        sb.append("\r\n");
        // appKey
        sb.append("--").append(mimeBoundary);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"appKey\"");
        sb.append("\r\n\r\n");
        sb.append(appKey);
        sb.append("\r\n");
        // mobile
        sb.append("--").append(mimeBoundary);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"mobile\"");
        sb.append("\r\n\r\n");
        sb.append(mobile);
        sb.append("\r\n");
        if (!StringUtil.isEmpty(ipAddress)) {
            // ipAddress
            sb.append("--").append(mimeBoundary);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"ipAddress\"");
            sb.append("\r\n\r\n");
            sb.append(ipAddress);
            sb.append("\r\n");
        }
        // body结束时 boundary前后各需添加两上横线，最添加添回车换行
        sb.append("--").append(mimeBoundary).append("--").append("\r\n");
        try {
            HttpResponse response = HttpUtil.sendDataViaHTTP(HttpUtil.HTTP_POST, url,
                HttpUtil.CONTENT_TYPE_FORM_DATA_WITH_BOUNDARY, sb.toString(), null);
            if (response.getResponseCode() == HttpUtil.STATUS_OK) {
                ChuangLanWoolCheckRsp rsp
                    = mapper.readValue(new String(response.getResponseBody()), ChuangLanWoolCheckRsp.class);
                if (ChuangLanWoolCheckRsp.SUCCES_CODE.equals(rsp.getCode())) {
                    return rsp;
                } else {
                    logger.debug("response = " + new String(response.getResponseBody()));
                    return null;
                }

            } else {
                logger.debug("response code = " + response.getResponseCode());
            }

        } catch (IOException e) {
            logger.debug("response with error, " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ChuangLanPhoneNumberRsp validateMobile(String mobile) {
        String apiName = PropertyManager.getProperty("jbb.mgt.cl.api.appId");
        String password = PropertyManager.getProperty("jbb.mgt.cl.api.appKey");
        String url = PropertyManager.getProperty("jbb.mgt.cl.api.url");
        url += "?appId=" + apiName + "&appKey=" + password + "&mobile=" + mobile;
        try {
            HttpResponse response
                = HttpUtil.sendDataViaHTTP(HttpUtil.HTTP_POST, url, HttpUtil.CONTENT_TYPE_JSON, null, null);
            if (response.getResponseCode() == HttpUtil.STATUS_OK) {
                ChuangLanPhoneNumberRsp rsp
                    = mapper.readValue(new String(response.getResponseBody()), ChuangLanPhoneNumberRsp.class);
                if (ChuangLanPhoneNumberRsp.SUCCES_CODE.equals(rsp.getCode())) {
                    return rsp;
                } else {
                    logger.debug("response = " + new String(response.getResponseBody()));
                    return null;
                }

            } else {
                logger.debug("response code = " + response.getResponseCode());
            }

        } catch (IOException e) {
            logger.debug("response with error, " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void sendMsgCode(String phoneNumber, String channelCode, String signName) {
        if (PropertyManager.contains("jbb.test.accounts", phoneNumber)) {
            logger.info("Test account require msgCode, phoneNumber=" + phoneNumber);
            return;
        }
        String account = PropertyManager.getProperty("jbb.mgt.msgcode.api.account");
        String pswd = PropertyManager.getProperty("jbb.mgt.msgcode.api.pswd");
        String smsSingleRequestServerUrl = PropertyManager.getProperty("jbb.mgt.msgcode.api.smsSingleRequestServerUrl");
        String msgCode = StringUtil.getRandomnum(4);
        String CodeTemplate = PropertyManager.getProperty("jbb.mgt.msgcode.api.template");
        String msg = signName + StringUtil.replace(CodeTemplate, "CodeTemplate", msgCode);
        //模板： "您好，您的验证码是" + msgCode + "，5分钟内有效，请妥善保管好验证码，防止泄露。"
        SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phoneNumber);
        String requestJson = JSON.toJSONString(smsSingleRequest);
        try {
            String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
            SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
            if (smsSingleResponse.getCode() != null && "0".equals(smsSingleResponse.getCode())) {
                long time = System.currentTimeMillis();
                messageCodeDao.saveMessageCode(phoneNumber, channelCode, msgCode, new Timestamp(time),
                    DateUtil.calTimestamp(time, 5 * 60 * 1000));
            } else {
                logger.warn(smsSingleResponse.getErrorMsg());
                throw new ChuangLanException("jbb.error.exception.chuanlan.limit", smsSingleResponse.getErrorMsg());
            }
        } catch (Exception e) {
            logger.error(e.toString());
            throw new ChuangLanException();
        }
    }

}
