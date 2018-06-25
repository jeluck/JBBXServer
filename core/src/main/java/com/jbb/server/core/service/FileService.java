 package com.jbb.server.core.service;

 public interface FileService {
     
     void saveUserAvatar(int userId, String fileName, byte[] content, String path);

}
