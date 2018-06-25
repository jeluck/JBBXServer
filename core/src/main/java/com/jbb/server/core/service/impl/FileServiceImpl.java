package com.jbb.server.core.service.impl;

import com.jbb.server.core.dao.AccountDao;
import com.jbb.server.core.service.FileService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service("fileService")
public class FileServiceImpl implements FileService {
    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private AccountDao accountDao;

    @Override
    public void saveUserAvatar(int userId, String fileName, byte[] content, String path) {

        if (logger.isDebugEnabled()) {
            logger.debug("|>saveUserAvatar() userId=" + userId + ", fileName=" + fileName + ", content size="
                + (content != null ? content.length : null));
        }

        try {
            initDirectory(path);
            String newName = getNewName(fileName, userId);
            File file = new File(path + "/" + newName);
            FileUtils.writeByteArrayToFile(file, content);
            accountDao.updateUserAvatar(userId, newName);
        } catch (IOException e) {
            logger.error(e.toString());
        }

        logger.debug("|<saveUserAvatar()");
    }

    /**
     * 初始化目录
     * 
     * @param path
     * @throws IOException
     */
    private void initDirectory(String path) throws IOException {
        File dir = new File(path);
        if (!dir.exists()) {
            FileUtils.forceMkdir(dir);
        }
    }

    private String getNewName(String fileName, int userId) {
        String newFileName = "Unkonwn";
        if (fileName != null) {
            int index = fileName.lastIndexOf(".");
            if (index != -1)
                newFileName = userId + fileName.substring(index);
        }

        return newFileName;
    }

}
