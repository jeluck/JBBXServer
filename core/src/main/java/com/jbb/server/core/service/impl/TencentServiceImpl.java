package com.jbb.server.core.service.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbb.server.common.Constants;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.ExecutionException;
import com.jbb.server.common.lang.LanguageHelper;
import com.jbb.server.common.util.HttpUtil;
import com.jbb.server.common.util.HttpUtil.HttpResponse;
import com.jbb.server.common.util.IOUtil;
import com.jbb.server.core.dao.UserVerifyDao;
import com.jbb.server.core.domain.LiveDetectForeResult;
import com.jbb.server.core.domain.LiveDetectFourResponse;
import com.jbb.server.core.domain.LiveDetectFourResponse.CompareData;
import com.jbb.server.core.domain.LiveGetFourResponse;
import com.jbb.server.core.domain.LiveGetFourResponse.ValidateData;
import com.jbb.server.core.service.AliyunService;
import com.jbb.server.core.service.TencentService;
import com.jbb.server.core.util.TencentSignUtil;

@Service("TencentService")
public class TencentServiceImpl implements TencentService {

    private static Logger logger = LoggerFactory.getLogger(TencentServiceImpl.class);

    @Autowired
    private AliyunService aliyunService;
    @Autowired
    private UserVerifyDao userVerifyDao;

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public String sign() {
        long appId = PropertyManager.getLongProperty("jbb.tencent.appid", 1256078225);
        String secretId = PropertyManager.getProperty("jbb.tencent.secretid");
        String secretKey = PropertyManager.getProperty("jbb.tencent.secretkey");
        String bucketName = PropertyManager.getProperty("jbb.tencent.bucket.name");
        long expired = 3600L;
        try {
            return TencentSignUtil.appSign(appId, secretId, secretKey, bucketName, expired);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return null;
    }

    @Override
    public LiveDetectForeResult liveDetectFour(int userId, String validateData, byte[] videoContent) {
        logger.debug("> liveDetectFour() userId = " + userId);
        String sign = sign();
        String urlStr = PropertyManager.getProperty("jbb.tencent.livedetectfour");
        String appid = PropertyManager.getProperty("jbb.tencent.appid");
        int sim = PropertyManager.getIntProperty("jbb.tencent.livedetectfour.sim", 75);

        String idCardKey = "idcard_" + userId + "_rear";
        byte[] picContent;
        try {
            picContent = aliyunService.getImageBytes(AliyunServiceImpl.BUCKET_IDCARD, idCardKey);
        } catch (Exception e1) {
            logger.info("liveDetectFour - not found idcard photo, userId = " + userId);
            return new LiveDetectForeResult(0, false,
                LanguageHelper.getLocalizedMessage("jbb.error.live.detect.code.notFoundIdCard", "zh"), null);
        }

        try {

            HttpResponse response =
                postLiveDetectFore(sign, userId, urlStr, appid, validateData, videoContent, picContent);
            if (response.getResponseCode() == HttpUtil.STATUS_OK) {
                LiveDetectFourResponse rsp =
                    mapper.readValue(new String(response.getResponseBody()), LiveDetectFourResponse.class);
                if (LiveDetectFourResponse.SUCCESS_CODE == rsp.getCode()) {
                    if(logger.isDebugEnabled()){
                        logger.debug("response = " + new String(response.getResponseBody()));
                    }
                    CompareData data = rsp.getData();
                    if (data != null) {

                        if (data.getSim() >= sim) {
                            savePhotoToOss(userId, data.getPhoto());
                            // 实名验证第三步，视频唇语核身
                            userVerifyDao.saveUserVerifyResult(userId, Constants.VERIFY_TYPE_VIDEO, 1, true);
                            return new LiveDetectForeResult(data.getSim(), true, null, null);
                        } else if (data.getSim() > 0 && data.getSim() < sim) {
                            String msg = LanguageHelper.getLocalizedMessage("jbb.error.live.detect.code.sim", "zh");
                            return new LiveDetectForeResult(data.getSim(), false, msg, null);
                        } else {
                            String compareMsg = data.getCompareStatus() == 0 ? null : LanguageHelper
                                .getLocalizedMessage("jbb.error.live.detect.code." + data.getCompareStatus(), "zh");
                            String liveVideoMsg = data.getLiveStatus() == 0 ? null : LanguageHelper
                                .getLocalizedMessage("jbb.error.live.detect.code." + data.getLiveStatus(), "zh");
                            return new LiveDetectForeResult(data.getSim(), false, compareMsg, liveVideoMsg);
                        }

                    }

                } else {
                    logger.debug("response = " + new String(response.getResponseBody()));
                    return new LiveDetectForeResult(0, false,
                        LanguageHelper.getLocalizedMessage("jbb.error.live.detect.code.apiCallError", "zh"), null);
                }

            } else {
                logger.debug("response code = " + response.getResponseCode());
                logger.debug("response content = " + new String(response.getResponseBody()));
            }

        } catch (IOException e) {
            logger.debug("response with error, " + e.getMessage());
            e.printStackTrace();
        }

        logger.debug("<liveDetectFour()");
        return new LiveDetectForeResult(0, false,
            LanguageHelper.getLocalizedMessage("jbb.error.live.detect.code.intenalError", "zh"), null);
    }

    private HttpResponse postLiveDetectFore(String sign, int userId, String urlStr, String appid, String validateData,
        byte[] videoContent, byte[] photoContent) throws IOException {

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
        conn.setRequestProperty("Authorization", sign);

        DataOutputStream ds = new DataOutputStream(conn.getOutputStream());

        // appid
        ds.writeBytes(twoHyphens + boundary + end);
        ds.writeBytes("Content-Disposition: form-data; name=\"appid\"");
        ds.writeBytes(end);
        ds.writeBytes(end);
        ds.writeBytes(appid);
        ds.writeBytes(end);

        // validate_data
        ds.writeBytes(twoHyphens + boundary + end);
        ds.writeBytes("Content-Disposition: form-data; name=\"validate_data\"");
        ds.writeBytes(end);
        ds.writeBytes(end);
        ds.writeBytes(validateData);
        ds.writeBytes(end);

        // compare_flag
        ds.writeBytes(twoHyphens + boundary + end);
        ds.writeBytes("Content-Disposition: form-data; name=\"compare_flag\"");
        ds.writeBytes(end);
        ds.writeBytes(end);
        ds.writeBytes("true");
        ds.writeBytes(end);

        // video
        ds.writeBytes(twoHyphens + boundary + end);
        ds.writeBytes("Content-Disposition: form-data; name=\"video\"; filename=\"video" + userId + ".mp4\"");
        ds.writeBytes(end);
        ds.writeBytes("Content-Type: video/mp4");
        ds.writeBytes(end);
        ds.writeBytes(end);
        ds.write(videoContent);
        ds.writeBytes(end);

        // card
        ds.writeBytes(twoHyphens + boundary + end);
        ds.writeBytes("Content-Disposition: form-data; name=\"card\"; filename=\"photo" + userId + ".jpeg\"");
        ds.writeBytes(end);
        ds.writeBytes("Content-Type: image/jpeg");
        ds.writeBytes(end);
        ds.writeBytes(end);
        ds.write(photoContent);
        ds.writeBytes(end);

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

    private void savePhotoToOss(int userId, String phoneStr) {
        aliyunService.putObject(AliyunServiceImpl.BUCKET_VIDEO_AND_PIC, "photo_" + userId, phoneStr);
    }

    @Override
    public String liveGetFour() {
        logger.debug("> liveGetFour()");
        String sign = sign();
        String urlStr = PropertyManager.getProperty("jbb.tencent.livegetfour");
        String appid = PropertyManager.getProperty("jbb.tencent.appid");

        String reqData = "{\"appid\":\"" + appid + "\"}";

        String[][] requestProperties = {{"Authorization", sign}};
        try {

            HttpResponse response = HttpUtil.sendDataViaHTTPRepeat(HttpUtil.HTTP_POST, urlStr,
                HttpUtil.CONTENT_TYPE_JSON, reqData, requestProperties, "liveGetFour");

            if (response.getResponseCode() == HttpUtil.STATUS_OK) {
                LiveGetFourResponse rsp =
                    mapper.readValue(new String(response.getResponseBody()), LiveGetFourResponse.class);
                if (LiveGetFourResponse.SUCCESS_CODE == rsp.getCode()) {
                    logger.debug("response = " + new String(response.getResponseBody()));
                    ValidateData data = rsp.getData();
                    if (data != null) {
                        return data.getValidateData();
                    }

                } else {
                    logger.debug(" liveGetFour response = " + new String(response.getResponseBody()));
                    return null;
                }

            } else {
                logger.debug("liveGetFour response code = " + response.getResponseCode());
                logger.debug("liveGetFour response content = " + new String(response.getResponseBody()));
            }

        } catch (IOException e) {
            logger.debug("liveGetFour response with error, " + e.getMessage());
            e.printStackTrace();
        }

        logger.debug("<liveGetFour()");
        return null;
    }

}
