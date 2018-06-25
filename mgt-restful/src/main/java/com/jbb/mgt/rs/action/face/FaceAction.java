package com.jbb.mgt.rs.action.face;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.IdCard;
import com.jbb.mgt.core.service.AliyunService;
import com.jbb.mgt.core.service.FaceService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.MissingParameterException;

/**
 * face++扫描身份证接口
 * 
 * @author inspiron
 * @date 2018年6月4日
 */

@Service(FaceAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FaceAction extends BasicAction {

    public static final String ACTION_NAME = "FaceAction";

    private static Logger logger = LoggerFactory.getLogger(FaceAction.class);

    @Autowired
    private FaceService faceService;

    @Autowired
    private AliyunService aliyunService;

    private Response response;

    public void ocrIdCard() {
        logger.debug(">ocrIdCard() userId = " + this.user.getUserId());
        // 从oss端获取图片信息
        String key = this.user.getIdcardRear();
        if (key == null || key.equals("")) {
            logger.error("Parameter IdCardKey Not Found");
            throw new MissingParameterException("jbb.mgt.face.photoKeyNotFound", "zh", key);
        }

        byte[] photoContent = null;
        try {
            String style = PropertyManager.getProperty("jbb.face.ocr.style");
            String bucket = PropertyManager.getProperty("jbb.aliyu.oss.bucket.jbb.mgt.user.photos");
            photoContent = aliyunService.getImageBytesWithStyle(bucket, key, style);
        } catch (IOException e) {
            logger.error("get oss photo error -->" + e);
        }
        if (photoContent == null || photoContent.length == 0) {
            logger.error("parameter photocontent is null");
            throw new MissingParameterException("jbb.mgt.face.photoKeyNotFound", "zh", key);
        }

        this.response.idcard = faceService.faceOCR(this.user.getUserId(), photoContent);
        logger.debug("<ocrIdCard()");
    }

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        public IdCard idcard;

        public IdCard getIdcard() {
            return idcard;
        }

    }

}
