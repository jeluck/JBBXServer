package com.jbb.server.core.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.cloudapi.sdk.core.model.ApiResponse;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.GenericRequest;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbb.server.common.Constants;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.AliyunException;
import com.jbb.server.common.exception.ApiCallLimitException;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.HttpUtil;
import com.jbb.server.common.util.HttpUtil.HttpResponse;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.dao.MessageCodeDao;
import com.jbb.server.core.dao.UserVerifyDao;
import com.jbb.server.core.domain.AliPolicy;
import com.jbb.server.core.domain.IdCard;
import com.jbb.server.core.domain.IdCardResult;
import com.jbb.server.core.service.AliyunService;
import com.jbb.server.core.service.UserEventLogService;
import com.jbb.third.aliyun.api.SyncApiClientOCRIdCard;

@Service("aliyunService")
public class AliyunServiceImpl implements AliyunService {
    private static Logger logger = LoggerFactory.getLogger(AliyunServiceImpl.class);

    public static String AVATAR_FILE_PREFIX = "user_avatar_";

    public static String IDCARD_FILE_PREFIX = "idcard_";

    public static String BUCKET_VIDEO_AND_PIC = "jbb-video-and-pic";
    public static String BUCKET_IDCARD = "jbb-idcards";

    private static SyncApiClientOCRIdCard syncClient = null;

    static {
        String appKey = PropertyManager.getProperty("jbb.api.ocridcard.appKey");
        String appSecret = PropertyManager.getProperty("jbb.api.ocridcard.appSecret");
        syncClient = SyncApiClientOCRIdCard.newBuilder().appKey(appKey).appSecret(appSecret).build();
    }

    @Autowired
    private MessageCodeDao messageCodeDao;

    @Autowired
    private UserVerifyDao userVerifyDao;

    @Autowired
    private UserEventLogService userEventLogService;

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public void sendCode(String phoneNumber) {
        // 如果是测试账户，直接返回
        if (PropertyManager.contains("jbb.test.accounts", phoneNumber)) {
            logger.info("Test account require msgCode, phoneNumber=" + phoneNumber);
            return;
        }

        // 初始化ascClient需要的几个参数
        final String product = "Dysmsapi";// 短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";// 短信API产品域名（接口地址固定，无需修改）
        // 替换成你的AK
        final String accessKeyId = PropertyManager.getProperty("jbb.aliyun.sms.accessKeyId");// 你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = PropertyManager.getProperty("jbb.aliyun.sms.accessKeySecret");// 你的accessKeySecret，参考本文档步骤2
        // 初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            logger.error(e.toString());
            throw new AliyunException();
        }

        String msgCode = StringUtil.getRandomnum(4);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        // 组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        // 使用post提交
        request.setMethod(MethodType.POST);
        // 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        request.setPhoneNumbers(phoneNumber);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName(PropertyManager.getProperty("jbb.aliyun.sms.signName"));
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(PropertyManager.getProperty("jbb.aliyun.sms.templateCode"));
        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        // 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam("{\"code\":" + msgCode + "}");
        // 可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        // request.setSmsUpExtendCode("90997");
        // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        // request.setOutId("yourOutId");
        // 请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                long time = System.currentTimeMillis();
                messageCodeDao.saveMessageCode(phoneNumber, msgCode, new Timestamp(time),
                    DateUtil.calTimestamp(time, 10 * 60 * 1000));
            } else {
                logger.warn(sendSmsResponse.getMessage());
                throw new AliyunException("jbb.error.exception.aliyun.limit", sendSmsResponse.getMessage());
            }
        } catch (ServerException e) {
            logger.error(e.toString());
            throw new AliyunException();
        } catch (ClientException e) {
            logger.error(e.toString());
            throw new AliyunException();
        }

    }

    @Override
    public AliPolicy getPostPolicy(int userId, String filePrifix) {
        String endpoint = PropertyManager.getProperty("jbb.aliyu.oss.bucket.endpoint");
        String accessId = PropertyManager.getProperty("jbb.aliyun.oss.accessKeyId");
        String accessKey = PropertyManager.getProperty("jbb.aliyun.oss.accessKeySecret");
        String bucket = null;
        if (IDCARD_FILE_PREFIX.equals(filePrifix)) {
            bucket = PropertyManager.getProperty("jbb.aliyu.oss.bucket.idcards");
        } else {
            bucket = PropertyManager.getProperty("jbb.aliyu.oss.bucket.name");
        }
        String dir = filePrifix + String.valueOf(userId);
        String host = "http://" + bucket + "." + endpoint;
        OSSClient client = new OSSClient(endpoint, accessId, accessKey);

        try {
            long expireTime = 3000;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 5242880);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);
            return new AliPolicy(accessId, encodedPolicy, postSignature, dir, host, expireEndTime / 1000);
        } catch (Exception e) {
            throw new AliyunException();
        }
    }

    @Override
    public String generatePresignedUrl(int userId, long expiration) {

        String endpoint = PropertyManager.getProperty("jbb.aliyu.oss.bucket.endpoint");
        String accessId = PropertyManager.getProperty("jbb.aliyun.oss.accessKeyId");
        String accessKey = PropertyManager.getProperty("jbb.aliyun.oss.accessKeySecret");
        String bucket = PropertyManager.getProperty("jbb.aliyu.oss.bucket.name");
        String key = AVATAR_FILE_PREFIX + String.valueOf(userId);
        OSSClient client = new OSSClient(endpoint, accessId, accessKey);
        try {
            client.getObjectMetadata(new GenericRequest(bucket, key));
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, key);
            request.setExpiration(new Date(expiration - (int)(Math.random()*1000000L)));
            request.setProcess("style/resize");
            URL url = client.generatePresignedUrl(request);
            return url.getFile();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            // do nothiong
        }
        return null;
    }

    @Override
    public IdCard ocrIdCard(int userId, Object body) {

        int apiLimitCnt = PropertyManager.getIntProperty("jbb.api.ocridcard.limit", 5);
        long start = DateUtil.getTodayStartTs();
        long end = start + DateUtil.DAY_MILLSECONDES;
        int cnt = userEventLogService.countEventLogByParams(userId, null, "user", "ocrIdCard", null, "1",
            new Timestamp(start), new Timestamp(end));
        if (cnt > apiLimitCnt) {
            throw new ApiCallLimitException("jbb.error.exception.api.limitwithdaynum", String.valueOf(apiLimitCnt));
        }
        ApiResponse response;
        IdCard idCard = null;
        String responseBodyStr = null;
        try {
            userEventLogService.insertEventLog(userId, null, "user", "ocrIdCard", null, "1", null);
            response = syncClient.ocrIdCard(mapper.writeValueAsBytes(body));
            responseBodyStr = new String(response.getBody(), "utf-8");
            idCard = mapper.readValue(responseBodyStr, IdCard.class);
            // 实名验证第一步， OCR扫描
            userVerifyDao.saveUserVerifyResult(userId, Constants.VERIFY_TYPE_REALNAME, 1, true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
        return idCard;
    }

    @Override
    public boolean checkIdCard(int userId, String idcardNumber, String username) {
        logger.debug(">checkIdCard, userId=" + userId + " ,idcardNumber=" + idcardNumber + " ,username=" + username);
        int apiLimitCnt = PropertyManager.getIntProperty("jbb.api.idcard3.limit", 5);
        long start = DateUtil.getTodayStartTs();
        long end = start + DateUtil.DAY_MILLSECONDES;
        int cnt = userEventLogService.countEventLogByParams(userId, null, "user", "idcard3", null, "1",
            new Timestamp(start), new Timestamp(end));
        if (cnt > apiLimitCnt) {
            throw new ApiCallLimitException("jbb.error.exception.api.limitwithdaynum", String.valueOf(apiLimitCnt));
        }
        try {
            userEventLogService.insertEventLog(userId, null, "user", "idcard3", null, "1", null);
            String url = PropertyManager.getProperty("jbb.api.idcard3.url");
            String appcode = PropertyManager.getProperty("jbb.api.idcard3.appcode");
            url += "?cardNo=" + idcardNumber + "&realName=" + username;
            String[][] requestProperties = {{"Accept-Charset", "utf-8"}, {"Authorization", "APPCODE " + appcode}};
            HttpResponse response = HttpUtil.sendDataViaHTTP(HttpUtil.HTTP_POST, url,
                HttpUtil.CONTENT_TYPE_X_WWW_FORM_URLENCODED, null, requestProperties);
            if (response.getResponseCode() == HttpUtil.STATUS_OK) {
                IdCardResult idcardResult = mapper.readValue(response.getResponseBody(), IdCardResult.class);
                logger.debug("<checkIdCard success. idcardResult = " + idcardResult);
                if (idcardResult.getErrorCode() == IdCardResult.SUCC_CODE) {
                    // 实名验证第二步，身份证核实
                    userVerifyDao.saveUserVerifyResult(userId, Constants.VERIFY_TYPE_REALNAME, 2, true);
                    return true;
                }
                return false;
            } else {
                logger.warn("<heckIdCard error, response code=" + response.getResponseCode()
                    + new String(response.getResponseBody()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
        }
        logger.debug("<checkIdCard");
        return false;
    }

    @Override
    public void putObject(String bucket, String key, String content) {
        putObject(bucket, key, content.getBytes(), null);
    }

    @Override
    public void putObject(String bucket, String key, byte[] content, String contentType) {
        // endpoint以杭州为例，其它region请按实际情况填写
        String endpoint = PropertyManager.getProperty("jbb.aliyu.oss.bucket.endpoint");
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建
        String accessKeyId = PropertyManager.getProperty("jbb.aliyun.oss.accessKeyId");
        String accessKeySecret = PropertyManager.getProperty("jbb.aliyun.sms.accessKeySecret");
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        ObjectMetadata metadata = null;
        if (StringUtil.isEmpty(contentType)) {
            metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
        }
        // 上传
        ossClient.putObject(bucket, key, new ByteArrayInputStream(content), metadata);
        // 关闭client
        ossClient.shutdown();
    }

    @Override
    public String getObject(String bucket, String key) throws IOException {
        // endpoint以杭州为例，其它region请按实际情况填写
        String endpoint = PropertyManager.getProperty("jbb.aliyu.oss.bucket.endpoint");
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建
        String accessKeyId = PropertyManager.getProperty("jbb.aliyun.oss.accessKeyId");
        String accessKeySecret = PropertyManager.getProperty("jbb.aliyun.sms.accessKeySecret");
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        OSSObject ossObject = ossClient.getObject(bucket, key);
        // 读Object内容
        System.out.println("Object content:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reader.close();
        // 关闭client
        ossClient.shutdown();
        return buffer.toString();
    }

    @Override
    public byte[] getImageBytes(String bucket, String key) throws IOException {
        // endpoint以杭州为例，其它region请按实际情况填写
        String endpoint = PropertyManager.getProperty("jbb.aliyu.oss.bucket.endpoint");
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建
        String accessKeyId = PropertyManager.getProperty("jbb.aliyun.oss.accessKeyId");
        String accessKeySecret = PropertyManager.getProperty("jbb.aliyun.sms.accessKeySecret");
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        OSSObject ossObject = ossClient.getObject(bucket, key);
        // 读Object内容
        System.out.println("Object content:");
        InputStream is = ossObject.getObjectContent();

        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = is.read(buff, 0, 100)) > 0) {
            byteArrayStream.write(buff, 0, rc);
        }
        byteArrayStream.close();
        // 关闭client
        ossClient.shutdown();
        return byteArrayStream.toByteArray();
    }
}
