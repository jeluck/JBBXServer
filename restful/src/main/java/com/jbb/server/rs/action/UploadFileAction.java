package com.jbb.server.rs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbb.server.common.PropertyManager;
import com.jbb.server.core.service.FileService;

/**
 * @Type LoginAction.java
 * @Desc
 * @author VincentTang
 * @date 2017年10月30日 下午6:08:16
 * @version
 */
@Service(UploadFileAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UploadFileAction extends BasicAction {
    private static Logger logger = LoggerFactory.getLogger(UploadFileAction.class);

    public static final String ACTION_NAME = "UploadFileAction";

    @Autowired
    private FileService fileService;

    public void saveUserAvatar(String fileName, byte[] content) {
        if ((content == null) || (content.length == 0)) {
            logger.warn("empty content");
            setResultCode(MISSED_PARAMETER, "empty content");
            logger.debug("<saveUserAvatar() empty content");
            return;
        }
        String path = PropertyManager.getProperty("jbb.user.avatar.path");
        fileService.saveUserAvatar(user.getUserId(), fileName, content, path);
    }
    
    

}