package com.jbb.mgt.core.service.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.dao.UserDao;
import com.jbb.mgt.core.domain.IdCard;
import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.core.service.FaceService;
import com.jbb.mgt.core.service.UserEventLogService;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.ApiCallLimitException;
import com.jbb.server.common.exception.ExecutionException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.HttpUtil;
import com.jbb.server.common.util.HttpUtil.HttpResponse;
import com.jbb.server.common.util.IOUtil;

import net.sf.json.JSONObject;

@Service("FaceService")
public class FaceServiceImpl implements FaceService {

    private static Logger logger = LoggerFactory.getLogger(FaceServiceImpl.class);

    @Autowired
    private UserEventLogService userEventLogService;

    @Autowired
    private UserDao userDao;

    @Override
    public IdCard faceOCR(int userId, byte[] photoContent) {
        logger.debug(">faceOCR");
        String apiKey = PropertyManager.getProperty("jbb.face.apikey");
        String apiSecret = PropertyManager.getProperty("jbb.face.apisecret");
        String urlStr = PropertyManager.getProperty("jbb.face.ocr.url");
        IdCard result = null;
        try {
            userEventLogService.insertEventLog(userId, "", "", "user", "ocrIdCard", "", "1", "",
                DateUtil.getCurrentTimeStamp());
            HttpResponse response = this.postOcr(apiKey, apiSecret, urlStr, userId, photoContent);
            if (response.getResponseCode() == HttpUtil.STATUS_OK) {
                byte[] byteArray = response.getResponseBody();
                if (byteArray == null || byteArray.length < 1) {
                    logger.error("byteArray must not be null or empty");
                    throw new IllegalArgumentException("this byteArray must not be null or empty");
                } else {
                    String strRead = new String(byteArray, "utf-8");
                    logger.debug("content = " + strRead);
                    JSONObject jsonObject = JSONObject.fromObject(strRead);
                    result = new IdCard();
                    if (strRead.indexOf("front") >= 0) {
                        result.setAddress(jsonObject.getString("address"));
                        result.setBirth(jsonObject.getString("birthday"));
                        result.setName(jsonObject.getString("name"));
                        result.setNationality(jsonObject.getString("race"));
                        result.setNum(jsonObject.getString("id_card_number"));
                        result.setSex(jsonObject.getString("gender"));
                        // 将信息保存起来
                        User user = userDao.selectUserById(userId);
                        user.setUserName(jsonObject.getString("name"));
                        user.setIdCard(jsonObject.getString("id_card_number"));
                        user.setIdcardAddress(jsonObject.getString("address"));
                        user.setRace(jsonObject.getString("race"));
                        userDao.updateUser(user);
                    } else {
                        // 背面图 未扫描到图像 抛出异常
                        logger.error("face not found in user photo");
                        throw new WrongParameterValueException("jbb.mgt.face.imageerror", "zh", "photoContent");
                    }
                }
            } else {
                byte[] byteArray = response.getResponseBody();
                String strRead = new String(byteArray, "utf-8");
                logger.debug("content = " + strRead);
                JSONObject jsonObject = JSONObject.fromObject(strRead);
                String errorMessage = jsonObject.getString("error_message");
                logger.error("response status != 200 and errormessage = " + errorMessage);
                if (errorMessage != null) {
                    if (errorMessage.indexOf(":") >= 0) {
                        throw new WrongParameterValueException(
                            "jbb.mgt.face." + errorMessage.substring(0, errorMessage.indexOf(":")), "zh");
                    } else {
                        throw new WrongParameterValueException("jbb.mgt.face." + errorMessage, "zh");
                    }

                } else {
                    // 未知错误
                    throw new WrongParameterValueException("jbb.mgt.face.unknowerror", "zh");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("<faceOCR");
        return result;
    }

    private HttpResponse postOcr(String apiKey, String apiSecret, String urlStr, int userId, byte[] photoContent)
        throws IOException {

        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "----jbbvideoandpic";

        URL url = new URL(urlStr);

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        // 发送POST请求必须设置如下两行
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        DataOutputStream ds = new DataOutputStream(conn.getOutputStream());

        // appid
        ds.writeBytes(twoHyphens + boundary + end);
        ds.writeBytes("Content-Disposition: form-data; name=\"api_key\"");
        ds.writeBytes(end);
        ds.writeBytes(end);
        ds.writeBytes(apiKey);
        ds.writeBytes(end);

        // api_secret
        ds.writeBytes(twoHyphens + boundary + end);
        ds.writeBytes("Content-Disposition: form-data; name=\"api_secret\"");
        ds.writeBytes(end);
        ds.writeBytes(end);
        ds.writeBytes(apiSecret);
        ds.writeBytes(end);

        // image
        if (photoContent != null && photoContent.length != 0) {
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\"photo" + userId + ".jpeg\"");
            ds.writeBytes(end);
            ds.writeBytes("Content-Type: image/jpeg");
            ds.writeBytes(end);
            ds.writeBytes(end);
            ds.write(photoContent);
            ds.writeBytes(end);
        }

        // end
        ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

        ds.flush();

        conn.connect();

        int responseCode = conn.getResponseCode();

        InputStream in = null;
        byte[] responseBody = null;
        String errorMessage = null;

        try {
            if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                in = conn.getInputStream();

                if (in == null) {
                    throw new ExecutionException(
                        "Cannot get response stream for urlString=[" + urlStr + "], responseCode=" + responseCode);
                }

                responseBody = IOUtil.readStreamClean(in);
            } else {
                in = conn.getErrorStream();
                if (in == null)
                    in = conn.getInputStream();

                if (in != null) {
                    responseBody = IOUtil.readStreamClean(in);
                }

                errorMessage = conn.getResponseMessage();

                if (logger.isDebugEnabled()) {
                    logger.debug("Response code: " + responseCode + ", " + errorMessage);
                }
            }
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception e) {
                logger.warn("Exception in closing input stream " + e.toString());
            }
        }

        return new HttpResponse(responseCode, responseBody, errorMessage);
    }

}
