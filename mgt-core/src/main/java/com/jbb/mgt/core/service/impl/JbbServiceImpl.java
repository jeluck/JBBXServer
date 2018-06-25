package com.jbb.mgt.core.service.impl;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.util.StringUtil;
import com.jbb.mgt.core.domain.IouStatus;
import com.jbb.mgt.core.domain.LoanRecordUpdateRsp;
import com.jbb.mgt.core.domain.ReMgtIou;
import com.jbb.mgt.core.domain.UserResponse;
import com.jbb.mgt.core.service.JbbService;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.JbbCallException;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.exception.ObjectNotFoundException;
import com.jbb.server.common.util.HttpUtil;
import com.jbb.server.common.util.HttpUtil.HttpResponse;

import net.sf.json.JSONObject;

@Service("JbbService")
public class JbbServiceImpl implements JbbService {
    private static Logger logger = LoggerFactory.getLogger(JbbServiceImpl.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public LoanRecordUpdateRsp updateIouStatus(IouStatus ret) {
        if (ret == null) {
            throw new ObjectNotFoundException("jbb.mgt.exception.parameterObject.notFound", "zh",
                "LoanRecordUpdateRet");
        }
        if (StringUtils.isEmpty(ret.getIouCode())) {
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "iouCode");
        }
        if (ret.getStatus() == null || ret.getStatus() == 0) {
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "status");
        }
        String appKey = PropertyManager.getProperty("jbb.app.server.appkey");
        String[][] heads = {{"API_KEY", appKey}};
        String url = PropertyManager.getProperty("jbb.app.server.basicUrl") + "/integrate/iou/" + ret.getIouCode()
            + "/status/" + ret.getStatus();

        if (!StringUtil.isEmpty(ret.getExtentionDate())) {
            url += "?extentionDate=" + ret.getExtentionDate();
        }

        try {
            HttpUtil.HttpResponse response =
                HttpUtil.sendDataViaHTTP(HttpUtil.HTTP_PUT, url, HttpUtil.CONTENT_TYPE_JSON, null, heads);
            if (response.getResponseCode() == HttpUtil.STATUS_OK) {
                LoanRecordUpdateRsp rsp =
                    mapper.readValue(new String(response.getResponseBody()), LoanRecordUpdateRsp.class);
                if (LoanRecordUpdateRsp.SUCCES_CODE.equals(rsp.getResultCode())) {
                    return rsp;
                } else {
                    logger.warn("response code = " + response.getResponseCode());
                    return rsp;
                }
            } else {
                String bodyStr = new String(response.getResponseBody());
                logger.warn("getUserReportByPhoneNumber() request =  " + url);
                logger.error("getUserReportByPhoneNumber() response with error, " + bodyStr);
                throw new JbbCallException("jbb.mgt.integrate.exception.jbbError", bodyStr);
            }
        } catch (IOException e) {
            logger.error("response with error, " + e.getMessage());
            e.printStackTrace();
            throw new JbbCallException("jbb.mgt.integrate.exception.jbbError", "IOException");
        }
    }

    @Override
    public UserResponse getUserReportByPhoneNumber(String phoneNumber) {
        String appkey = PropertyManager.getProperty("jbb.app.server.appkey");
        String url = PropertyManager.getProperty("jbb.app.server.basicUrl") + "/integrate/user/sync";
        String[][] heads = {{"API_KEY", appkey}};
        url += "?phoneNumber=" + phoneNumber;
        try {
            HttpResponse response =
                HttpUtil.sendDataViaHTTP(HttpUtil.HTTP_POST, url, HttpUtil.CONTENT_TYPE_JSON, null, heads);
            if (response.getResponseCode() == HttpUtil.STATUS_OK) {
                UserResponse rsp = mapper.readValue(new String(response.getResponseBody()), UserResponse.class);
                if (UserResponse.SUCCES_CODE == rsp.getResultCode()) {
                    return rsp;
                } else {
                    logger.warn("response = " + new String(response.getResponseBody()));
                    return null;
                }
            } else {
                String bodyStr = new String(response.getResponseBody());
                logger.warn("getUserReportByPhoneNumber() request =  " + url);
                logger.error("getUserReportByPhoneNumber() response with error, " + bodyStr);
                throw new JbbCallException("jbb.mgt.integrate.exception.jbbError", bodyStr);
            }

        } catch (IOException e) {
            logger.error("response with error, " + e.getMessage());
            e.printStackTrace();
            throw new JbbCallException("jbb.mgt.integrate.exception.jbbError", "IOException");
        }
    }

    @Override
    public Integer check(Integer jbbUserId, String nickname) {
        String url = PropertyManager.getProperty("jbb.app.server.basicUrl") + "/integrate/user/check";
        url += "?userId=" + jbbUserId + "&userName=" + HttpUtil.encodeURLParam(nickname);
        String appkey = PropertyManager.getProperty("jbb.app.server.appkey");
        String[][] heads = {{"API_KEY", appkey}};
        int resultCode = -1;
        try {
            HttpResponse response =
                HttpUtil.sendDataViaHTTP(HttpUtil.HTTP_GET, url, HttpUtil.CONTENT_TYPE_JSON, null, heads);
            if (response.getResponseCode() == HttpUtil.STATUS_OK) {
                UserResponse rsp = mapper.readValue(new String(response.getResponseBody()), UserResponse.class);
                resultCode = rsp.getResultCode();
            } else {
                String bodyStr = new String(response.getResponseBody());
                logger.warn("check() request =  " + url);
                logger.error("check() response with error, " + bodyStr);
                throw new JbbCallException("jbb.mgt.integrate.exception.jbbError", bodyStr);
            }
        } catch (IOException e) {
            logger.error("check() response with error, " + e.getMessage());
            e.printStackTrace();
            throw new JbbCallException("jbb.mgt.integrate.exception.jbbError", "IOException");
        }
        return resultCode;
    }

    @Override
    public String fillIou(ReMgtIou reMgtIou) {
        String appkey = PropertyManager.getProperty("jbb.app.server.appkey");
        String url = PropertyManager.getProperty("jbb.app.server.basicUrl");
        String[][] heads = {{"API_KEY", appkey}};
        JSONObject jsonObject = JSONObject.fromObject(reMgtIou);
        String iouCode = null;
        try {
            HttpUtil.HttpResponse httpResponse = HttpUtil.sendDataViaHTTP(HttpUtil.HTTP_POST,
                url + "/integrate/fill/iou", HttpUtil.CONTENT_TYPE_JSON, jsonObject.toString(), heads);
            String bodyStr = new String(httpResponse.getResponseBody());
            bodyStr = new String(bodyStr.getBytes(),"UTF-8");
            JSONObject object = JSONObject.fromObject(bodyStr);
            if (httpResponse.getResponseCode() == HttpUtil.STATUS_OK) {
                if (object.getInt("resultCode") == 0) {
                    iouCode = object.getString("iouCode");
                }else{
                    logger.warn("fillIou() request =  " + reMgtIou);
                    logger.warn("fillIou() response =  " + new String(bodyStr));
                    String msg = object.getString("resultCodeMessage");
                    throw new JbbCallException("jbb.mgt.integrate.exception.jbbError", msg);
                }
            } else {
                logger.warn("fillIou() request =  " + reMgtIou);
                logger.warn("fillIou() response =  " + new String(bodyStr));
                String msg = object.getString("resultCodeMessage");
                throw new JbbCallException("jbb.mgt.integrate.exception.jbbError", msg);
            }
        } catch (IOException e) {
            logger.warn("fillIou() request =  " + reMgtIou);
            logger.error("fillIou() response with error, " + e.getMessage());
            throw new JbbCallException("jbb.mgt.integrate.exception.jbbError", "IOException");
        }
        return iouCode;
    }

    @Override
    public Integer checkSend(Integer accountId, Integer jbbUserId) {
        String url = PropertyManager.getProperty("jbb.app.server.basicUrl") + "/integrate/user/account";
        url += "?userId=" + jbbUserId + "&accountId=" + accountId;
        String appkey = PropertyManager.getProperty("jbb.app.server.appkey");
        String[][] heads = {{"API_KEY", appkey}};
        int resultCode = -1;
        try {
            HttpResponse response =
                HttpUtil.sendDataViaHTTP(HttpUtil.HTTP_PUT, url, HttpUtil.CONTENT_TYPE_JSON, null, heads);
            if (response.getResponseCode() == HttpUtil.STATUS_OK) {
                UserResponse rsp = mapper.readValue(new String(response.getResponseBody()), UserResponse.class);
                resultCode = rsp.getResultCode();
            } else {
                String bodyStr = new String(response.getResponseBody());
                logger.warn("checkSend() request =  " + url);
                logger.error("checkSend() response with error, " + bodyStr);
                throw new JbbCallException("jbb.mgt.integrate.exception.jbbError", bodyStr);
            }
        } catch (IOException e) {
            logger.error("check() response with error, " + e.getMessage());
            e.printStackTrace();
            throw new JbbCallException("jbb.mgt.integrate.exception.jbbError", "IOException");
        }
        return resultCode;
    }
}
