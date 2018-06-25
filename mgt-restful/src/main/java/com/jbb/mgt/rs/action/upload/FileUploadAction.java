
package com.jbb.mgt.rs.action.upload;

import com.jbb.mgt.core.domain.User;
import com.jbb.server.common.exception.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.service.AliyunService;
import com.jbb.mgt.core.service.UserService;
import com.jbb.mgt.core.service.impl.AliyunServiceImpl;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.StringUtil;

@Service(FileUploadAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FileUploadAction extends BasicAction {

    public static final String ACTION_NAME = "FileUploadAction";

    private static final int MAX_CONTENT_LENGTH_M = 10;
    private static final int MAX_CONTENT_LENGTH = MAX_CONTENT_LENGTH_M * 1024 * 1024;

    private static final int MAX_LOGO_CONTENT_LENGTH_KB = 500;
    private static final int MAX_LOGO_CONTENT_LENGTH = MAX_LOGO_CONTENT_LENGTH_KB * 1024;

    private static Logger logger = LoggerFactory.getLogger(FileUploadAction.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AliyunService aliyunservie;

    private Response response;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    /**
     * 上传组织图处，上传组织logo和背景
     *
     * @param content
     * @param type 1.背景 2.logo
     */
    public void upload(byte[] content, Integer type) {
        logger.debug(">upload()");
        if ((content == null) || (content.length == 0)) {
            logger.debug("<upload() missing file content");
            throw new WrongParameterValueException("jbb.mgt.exception.file.empty", "zh", "content");
        }
        if (content.length > MAX_LOGO_CONTENT_LENGTH) {
            logger.debug("<upload() file content > MAX_CONTENT_LENGTH");
            throw new WrongParameterValueException("jbb.mgt.exception.file.maxLength.kb", "zh",
                String.valueOf(MAX_LOGO_CONTENT_LENGTH_KB));
        }
        if (null == type || type == 0) {
            logger.debug("<upload() missing type");
            throw new WrongParameterValueException("jbb.mgt.error.exception.missing.param", "zh", "type");
        }
        if (type > 2) {
            logger.debug("<upload() error type");
            throw new WrongParameterValueException("jbb.mgt.error.exception.wrong.paramvalue", "zh", "type");
        }
        String fileExtend = "";
        if (type == 1) {
            fileExtend = "_bg.jpg";
        } else if (type == 2) {
            fileExtend = "_logo.png";
        }
        String fileName = account.getOrgId() + fileExtend;
        aliyunservie.putObject(AliyunServiceImpl.BUCKET_ORG_PHOTOS, fileName, content, "image/jpeg");
        this.response.fileName = fileName;
        logger.debug("<upload()");
    }

    /**
     * 身份证上传
     *
     * @param content
     * @param type 1.正面 2.反面 3.手持
     */
    public void uploadUserIdCard(byte[] content, Integer type) {
        logger.debug(">uploadUserIdCard()");
        if (null == type || type == 0) {
            logger.debug("<uploadUserIdCard() missing type");
            throw new WrongParameterValueException("jbb.mgt.error.exception.missing.param", "zh", "type");
        }
        if (type > 3) {
            logger.debug("<uploadUserIdCard() error type");
            throw new WrongParameterValueException("jbb.mgt.error.exception.wrong.paramvalue", "zh", "type");
        }
        if ((content == null) || (content.length == 0)) {
            logger.debug("<uploadUserIdCard() missing file content");
            throw new WrongParameterValueException("jbb.mgt.exception.file.empty", "zh", "content");
        }
        if (content.length > MAX_CONTENT_LENGTH) {
            logger.debug("<uploadUserIdCard() file content > MAX_CONTENT_LENGTH");
            throw new WrongParameterValueException("jbb.mgt.exception.file.maxLength", "zh",
                String.valueOf(MAX_CONTENT_LENGTH_M));
        }

        String fileName = this.user.getUserId() + "_" + StringUtil.getRandomnum(16) + ".jpg";

        aliyunservie.putObject(AliyunServiceImpl.BUCKET_USER_PHOTOS, fileName, content, "image/jpeg");

        if (type == 1) {
            this.user.setIdcardRear(fileName);
        } else if (type == 2) {
            this.user.setIdcardBack(fileName);
        } else if (type == 3) {
            this.user.setIdcardInfo(fileName);
        }
        userService.updateUser(this.user);
        this.response.fileName = fileName;
        logger.debug("<uploadUserIdCard()");
    }

    /**
     * 上传视频截图
     *
     * @param content
     * @param type
     * @param userId
     */
    public void uploadScreenShot(byte[] content, Integer type, Integer userId) {
        if (null == userId || userId == 0) {
            logger.debug("<uploadScreenshot() missing type");
            throw new WrongParameterValueException("jbb.mgt.error.exception.missing.param", "zh", "userId");
        }
        if ((content == null) || (content.length == 0)) {
            logger.debug("<uploadScreenshot() missing file content");
            throw new WrongParameterValueException("jbb.mgt.exception.file.empty", "zh", "content");
        }
        if (content.length > MAX_CONTENT_LENGTH) {
            logger.debug("<uploadScreenshot() file content > MAX_CONTENT_LENGTH");
            throw new WrongParameterValueException("jbb.mgt.exception.file.maxLength", "zh",
                String.valueOf(MAX_CONTENT_LENGTH_M));
        }
        User user = userService.selectUserById(userId);
        if (user == null) {
            throw new ObjectNotFoundException("jbb.mgt.exception.userNotFound", "zh", "userId:" + userId);
        }

        String fileName = user.getUserId() + "_" + StringUtil.getRandomnum(16) + ".jpg";
        aliyunservie.putObject(AliyunServiceImpl.BUCKET_USER_PHOTOS, fileName, content, "image/jpeg");

        user.setVidoeScreenShot(fileName);
        userService.updateUser(user);
        this.response.fileName = fileName;
        logger.debug("<uploadScreenshot()");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {

        private String fileName;

        public String getFileName() {
            return fileName;
        }

    }

}
