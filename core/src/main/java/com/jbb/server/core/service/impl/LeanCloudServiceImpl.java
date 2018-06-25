package com.jbb.server.core.service.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbb.server.common.Constants;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.util.CodecUtil;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.HttpUtil;
import com.jbb.server.common.util.HttpUtil.HttpResponse;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.dao.AccountDao;
import com.jbb.server.core.dao.UserDeviceDao;
import com.jbb.server.core.dao.UserPrivDao;
import com.jbb.server.core.domain.LcCmdType;
import com.jbb.server.core.domain.LcMessage;
import com.jbb.server.core.domain.LcMessageAttrs;
import com.jbb.server.core.domain.LcMessageExchangeInfo;
import com.jbb.server.core.domain.LcTextMessage;
import com.jbb.server.core.domain.Message;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserDevice;
import com.jbb.server.core.domain.UserKey;
import com.jbb.server.core.doman.notification.LeanCloudPayload;
import com.jbb.server.core.doman.notification.LeanCloudPayload.AndroidAlertData;
import com.jbb.server.core.doman.notification.LeanCloudPayload.IosAlertData;
import com.jbb.server.core.doman.notification.NotificationRequest;
import com.jbb.server.core.service.LeanCloudService;

@Service("LeanCloudService")
public class LeanCloudServiceImpl implements LeanCloudService {
    private static Logger logger = LoggerFactory.getLogger(LeanCloudServiceImpl.class);

    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String JBB_SYS_ADMIN = "JBB_SYS_ADMIN";

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private UserPrivDao userPrivDao;

    @Autowired
    private UserDeviceDao userDeviceDao;

    @Override
    public void sendMessage(LcTextMessage lcMessage) {
        sendOrUpdateMessage(lcMessage, true);

    }

    public void sendOrUpdateMessage(LcTextMessage lcMessage, boolean isNewMsg) {
        String lcId = PropertyManager.getProperty("jbb.leancloud.id");
        String masterKey = PropertyManager.getProperty("jbb.leancloud.masterkey") + ",master";
        String url = isNewMsg ? PropertyManager.getProperty("jbb.leancloud.sendmessage.url")
            : PropertyManager.getProperty("jbb.leancloud.updatemessage.url");
        String[][] requestProperties = {{"X-LC-Id", lcId}, {"X-LC-Key", masterKey}};

        try {
            String jsonData = mapper.writeValueAsString(lcMessage);
            if (logger.isDebugEnabled()) {
                logger.debug("isNewMsg=", isNewMsg);
                logger.debug("sendOrUpdateMessage body=", jsonData);
            }

            HttpResponse response = HttpUtil.sendDataViaHTTPRepeat(HttpUtil.HTTP_POST, url, HttpUtil.CONTENT_TYPE_JSON,
                jsonData, requestProperties, "sendOrUpdateMessage");

            if (response.isOkResponseCode()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("sendOrUpdateMessage success", new String(response.getResponseBody()));
                }
            } else {
                logger.error("sendOrUpdateMessage error", response.getErrorMessage());
            }
        } catch (JsonProcessingException e) {
            logger.error("sendOrUpdateMessage error", e);
        }

    }

    private String getSignature(String input, String key) {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1);
        String signature = null;
        try {
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(input.getBytes());
            signature = new String(CodecUtil.toHex(rawHmac));
        } catch (InvalidKeyException e) {
            logger.error("getLoginSignature error", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("getLoginSignature error", e);
        }
        return signature;
    }

    @Override
    public String getLoginSignature(String clientId, String nonce, long timestamp) {
        String appid = PropertyManager.getProperty("jbb.leancloud.id");
        String masterKey = PropertyManager.getProperty("jbb.leancloud.masterkey");
        String str = appid + ":" + clientId + "::" + timestamp + ":" + nonce;
        String signature = getSignature(str, masterKey);
        if (logger.isDebugEnabled()) {
            logger.debug(str + "->" + signature);
        }
        return signature;
    }

    @Override
    public String getConvSignature(String clientId, String sortedMemberIds, String nonce, long timestamp, String convId,
        String action) {

        String appid = PropertyManager.getProperty("jbb.leancloud.id");
        String masterKey = PropertyManager.getProperty("jbb.leancloud.masterkey");
        String str = appid + ":" + clientId + (StringUtil.isEmpty(convId) ? "" : (":" + convId)) + ":" + sortedMemberIds
            + ":" + timestamp + ":" + nonce
            + ((StringUtil.isEmpty(action) || "create".equals(action)) ? "" : (":" + action));
        String signature = getSignature(str, masterKey);
        if (logger.isDebugEnabled()) {
            logger.debug(str + "->" + signature);
        }
        return signature;
    }

    private String writeMessageValueAsString(LcMessage lcMsg) {
        try {
            String str = mapper.writeValueAsString(lcMsg);
            System.out.println(str);
            return str;
        } catch (JsonProcessingException e) {
            logger.warn("exchangeInfo error:", e);
        }
        return "";
    }

    private LcTextMessage createExchangeInfo(User user, User toUser, LcCmdType type, String convId) {
        LcMessageAttrs msgAttr = new LcMessageAttrs();
        LcMessageExchangeInfo fromClientInfo = new LcMessageExchangeInfo(user.getUserId());
        LcMessageExchangeInfo toClientInfo = new LcMessageExchangeInfo(toUser.getUserId());
        switch (type) {
            // 生成交换信息
            case QQAPPROVE:
                msgAttr.setCmdType(LcCmdType.QQINFO.getIndex());
                fromClientInfo.setQqNumber(user.getQqNumber());
                toClientInfo.setQqNumber(toUser.getQqNumber());
                break;
            case INFOAPPROVE:
                msgAttr.setCmdType(LcCmdType.USERINFO.getIndex());
                fromClientInfo.setUserBasic(user);
                toClientInfo.setUserBasic(toUser);
                break;
            case PHONEAPPROVE:
                msgAttr.setCmdType(LcCmdType.PHONEINFO.getIndex());
                fromClientInfo.setPhoneNumber(user.getPhoneNumber());
                toClientInfo.setPhoneNumber(toUser.getPhoneNumber());
                break;
            case WECHATAPPROVE:
                msgAttr.setCmdType(LcCmdType.WECHATINFO.getIndex());
                fromClientInfo.setWechat(user.getWechat());
                toClientInfo.setWechat(toUser.getWechat());
                break;
            default:
                break;
        }
        msgAttr.setFromClientInfo(fromClientInfo);
        msgAttr.setToClientInfo(toClientInfo);
        LcMessage message = new LcMessage(msgAttr);

        System.out.println(message);
        LcTextMessage lcTextMsg =
            new LcTextMessage(JBB_SYS_ADMIN, toUser.getUserId(), writeMessageValueAsString(message), convId, false);

        return lcTextMsg;
    }

    private List<LcTextMessage> createMessages(int from, int to, int cmdType, String convId) {
        LcCmdType type = LcCmdType.getType(cmdType);
        List<LcTextMessage> msgs = new ArrayList<LcTextMessage>();
        LcMessage message = null;
        LcTextMessage lcTextMsg = null;
        LcMessageAttrs msgAttr = null;
        // 反馈信息给from
        msgAttr = new LcMessageAttrs();
        msgAttr.setCmdType(cmdType);
        LcMessageExchangeInfo fromClientInfo = new LcMessageExchangeInfo(from);
        LcMessageExchangeInfo toClientInfo = new LcMessageExchangeInfo(to);
        msgAttr.setFromClientInfo(fromClientInfo);
        msgAttr.setToClientInfo(toClientInfo);
        message = new LcMessage(msgAttr);
        lcTextMsg = new LcTextMessage(JBB_SYS_ADMIN, from, writeMessageValueAsString(message), convId, false);
        msgs.add(lcTextMsg);

        if (type == LcCmdType.QQAPPROVE || type == LcCmdType.INFOAPPROVE || type == LcCmdType.PHONEAPPROVE
            || type == LcCmdType.WECHATAPPROVE) {
            // 生成交换信息
            User fromUser = accountDao.getUser(from);
            User toUser = accountDao.getUser(to);
            lcTextMsg = createExchangeInfo(fromUser, toUser, type, convId);
            msgs.add(lcTextMsg);
        }

        return msgs;
    }

    private void updateUserPriv(int from, int to, int cmdType) {
        LcCmdType type = LcCmdType.getType(cmdType);
        switch (type) {
            case QQAPPROVE:
                userPrivDao.saveUserPriv(from, to, Constants.USER_PRIV_QQ, true);
                userPrivDao.saveUserPriv(to, from, Constants.USER_PRIV_QQ, true);
                break;
            case PHONEAPPROVE:
                userPrivDao.saveUserPriv(from, to, Constants.USER_PRIV_PHONE, true);
                userPrivDao.saveUserPriv(to, from, Constants.USER_PRIV_PHONE, true);
                break;
            case WECHATAPPROVE:
                userPrivDao.saveUserPriv(from, to, Constants.USER_PRIV_WECHAT, true);
                userPrivDao.saveUserPriv(to, from, Constants.USER_PRIV_WECHAT, true);
                break;
            case INFOAPPROVE:
                userPrivDao.saveUserPriv(to, from, Constants.USER_PRIV_INFO, true);
                break;
            // case QQREJECT:
            // userPrivDao.saveUserPriv(userId, applyUserId, Constants.USER_PRIV_QQ, false);
            // userPrivDao.saveUserPriv(applyUserId, userId, Constants.USER_PRIV_QQ, false);
            // break;
            // case INFOREJECT:
            // userPrivDao.saveUserPriv(userId, applyUserId, Constants.USER_PRIV_WECHAT, false);
            // break;
            // case PHONEREJECT:
            // userPrivDao.saveUserPriv(userId, applyUserId, Constants.USER_PRIV_PHONE, false);
            // userPrivDao.saveUserPriv(applyUserId, userId, Constants.USER_PRIV_PHONE, false);
            // break;
            // case WECHATREJECT:
            // userPrivDao.saveUserPriv(userId, applyUserId, Constants.USER_PRIV_WECHAT, false);
            // userPrivDao.saveUserPriv(applyUserId, userId, Constants.USER_PRIV_WECHAT, false);
            // break;
            default:
                break;
        }
    }

    // 用户批准或者拒绝后，更新原来的提示消息
    private void markOldMsgDone(String oldMsgId, long oldMsgTs, String convId, int from, int to, int cmdType) {
        logger.debug("|>markOldMsgDone, oldMsgId =" + oldMsgId);
        LcCmdType type = LcCmdType.getType(cmdType);
        int preCmdType = 0;
        String action = "";
        if (type == LcCmdType.QQAPPROVE || type == LcCmdType.INFOAPPROVE || type == LcCmdType.PHONEAPPROVE
            || type == LcCmdType.WECHATAPPROVE) {
            preCmdType = type.getIndex() - 1;
            action = "approve";
        } else if (type == LcCmdType.QQREJECT || type == LcCmdType.INFOREJECT || type == LcCmdType.PHONEREJECT
            || type == LcCmdType.WECHATREJECT) {
            preCmdType = type.getIndex() - 2;
            action = "reject";
        }

        LcMessageAttrs msgAttr = new LcMessageAttrs();
        msgAttr.setCmdType(preCmdType);
        msgAttr.setAction(action);
        LcMessageExchangeInfo fromClientInfo = new LcMessageExchangeInfo(to);
        LcMessageExchangeInfo toClientInfo = new LcMessageExchangeInfo(from);
        msgAttr.setFromClientInfo(fromClientInfo);
        msgAttr.setToClientInfo(toClientInfo);
        LcMessage message = new LcMessage(msgAttr);
        LcTextMessage lcTextMsg =
            new LcTextMessage(JBB_SYS_ADMIN, writeMessageValueAsString(message), convId, oldMsgId, oldMsgTs, false);
        sendOrUpdateMessage(lcTextMsg, false);
        logger.debug("|<markOldMsgDone");
    }

    @Override
    public void exchangeInfo(int from, int to, int cmdType, String convId, String oldMsgId, Long oldMsgTs) {
        List<LcTextMessage> messages = createMessages(from, to, cmdType, convId);
        for (LcTextMessage message : messages) {
            sendMessage(message);
        }
        updateUserPriv(from, to, cmdType);
        if (!StringUtil.isEmpty(oldMsgId)) {
            markOldMsgDone(oldMsgId, oldMsgTs, convId, from, to, cmdType);
        }
    }

    private List<LeanCloudPayload> generatePushNotification(NotificationRequest req) {

        List<LeanCloudPayload> list = new ArrayList<LeanCloudPayload>();

        Message message = req.getMessage();

        int userId = message.getToUserId();
        
        //检查用户是否登录状态
//        UserKey userKey = accountDao.getUserKey(userId, UserKey.APPLICATION_USER, null);
//        if(userKey.getExpiry().before(DateUtil.getCurrentTimeStamp())){
//            return null;
//        }
        
        List<UserDevice> devices = userDeviceDao.selectUserDeviceListByUserId(userId);

        for (UserDevice device : devices) {
            
            
            LeanCloudPayload payload = new LeanCloudPayload();
            payload.setCql("select * from _Installation where objectId='" + device.getObjectId() + "'");
            if (device.isAndroid()) {
                AndroidAlertData alert = new AndroidAlertData();
                alert.setAlert(message.getMessageText());
                alert.setTitle(message.getSubject());
                payload.setData(alert);
            } else if (device.isIos()) {
                IosAlertData alert = new IosAlertData();
                alert.setAlert(message.getSubject() + "\r\n" + message.getMessageText());
                alert.setBadge("Increment");
                payload.setData(alert);
                payload.setProd(PropertyManager.getProperty("jbb.platform.ios.push"));
            }
            list.add(payload);
        }

        return list;
    }

    private void pushNotification(LeanCloudPayload pushData) {
        String lcId = PropertyManager.getProperty("jbb.leancloud.id");
        String appKey = PropertyManager.getProperty("jbb.leancloud.appKey");
        String url = PropertyManager.getProperty("jbb.leancloud.push.url");
        String[][] requestProperties = {{"X-LC-Id", lcId}, {"X-LC-Key", appKey}};

        try {
            String jsonData = mapper.writeValueAsString(pushData);
            if (logger.isDebugEnabled()) {
                logger.debug("pushNotification=", jsonData);
            }

            HttpResponse response = HttpUtil.sendDataViaHTTPRepeat(HttpUtil.HTTP_POST, url, HttpUtil.CONTENT_TYPE_JSON,
                jsonData, requestProperties, "pushNotification");

            if (response.isOkResponseCode()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("pushNotification success", new String(response.getResponseBody()));
                }
            } else {
                logger.error("pushNotification error", response.getErrorMessage());
            }
        } catch (JsonProcessingException e) {
            logger.error("pushNotification error", e);
        }
    }

    @Override
    public void pushNotification(NotificationRequest notificationReq) {

        List<LeanCloudPayload> pushDataList = generatePushNotification(notificationReq);
        if(pushDataList == null || pushDataList.size() == 0){
            return ;
        }
        for (LeanCloudPayload pushData : pushDataList) {
            pushNotification(pushData);
        }

    }

}
