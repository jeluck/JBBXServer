package com.jbb.server.core.service;

import java.io.IOException;

import com.jbb.server.core.domain.AliPolicy;
import com.jbb.server.core.domain.IdCard;

public interface AliyunService {

    void sendCode(String phoneNubmer);

    AliPolicy getPostPolicy(int userId, String filePrefix);

    String generatePresignedUrl(int userId, long expiration);

    IdCard ocrIdCard(int userId, Object body);

    boolean checkIdCard(int userId, String idcardNumber, String username);

    String getObject(String bucket, String key) throws IOException;

    void putObject(String bucket, String key, byte[] content, String contentType);

    void putObject(String bucket, String key, String content);

    byte[] getImageBytes(String bucket, String key) throws IOException;

}
