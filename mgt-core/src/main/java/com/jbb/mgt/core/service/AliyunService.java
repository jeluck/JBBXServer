package com.jbb.mgt.core.service;

import java.io.IOException;
import java.util.Map;

import com.jbb.mgt.core.domain.AliPolicy;
import com.jbb.mgt.core.domain.IPAddressInfo;

public interface AliyunService {

    void putObject(String bucket, String key, String content);

    String getObject(String bucket, String key) throws IOException;

    void sendSms(String phoneNumber, String templateCode, Map<String, String> params);

    AliPolicy getPostPolicy(int userId, String filePrefix);

    IPAddressInfo getIPAddressInfo(String ipAddress);

    void putObject(String bucket, String key, byte[] content, String contentType);

    void sendCode(String phoneNumber, String channelCode);

    void sendCode(String phoneNumber, String channelCode, String signName, String signTemplate);

    void afsCheck(String sessionId, String sig, String token, String scene, String remoteIp);
    
    byte[] getImageBytes(String bucket, String key) throws IOException;

    byte[] getImageBytesWithStyle(String bucket, String key, String style) throws IOException;
}
