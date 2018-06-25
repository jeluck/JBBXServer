package com.jbb.server.rs.action;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.common.Constants;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.ApiCallLimitException;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.domain.LiveDetectForeResult;
import com.jbb.server.core.service.ProductService;
import com.jbb.server.core.service.TencentService;
import com.jbb.server.core.service.UserEventLogService;
import com.jbb.server.rs.pojo.ActionResponse;

@Service(LiveDetectAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LiveDetectAction extends BasicAction {
    private static Logger logger = LoggerFactory.getLogger(LiveDetectAction.class);
    public static final String ACTION_NAME = "LiveDetectAction";

    private Response response;

    @Autowired
    private TencentService tencentService;

    @Autowired
    private UserEventLogService userEventLogService;

    @Autowired
    private ProductService productService;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    public void liveDetectGetFour() {
        this.response.validateData = tencentService.liveGetFour();
    }

    public void liveDetect(String validateData, byte[] content) {
        int fee = PropertyManager.getIntProperty("jbb.wx.pay.auth.fee", 0);
        if (fee != 0 && productService.getProductCount(this.user.getUserId(), Constants.PRODUCT_AUTH) == 0) {
            throw new ApiCallLimitException("jbb.error.exception.auth.notPay");
        }

        // 检查是否超出每日限制
        int userId = this.user.getUserId();
        int apiLimitCnt = PropertyManager.getIntProperty("jbb.api.video.validate.limit", 5);
        long start = DateUtil.getTodayStartTs();
        long end = start + DateUtil.DAY_MILLSECONDES;
        int cnt = userEventLogService.countEventLogByParams(userId, null, "user", "videoValidate", null, "1",
            new Timestamp(start), new Timestamp(end));
        if (cnt >= apiLimitCnt) {
            throw new ApiCallLimitException("jbb.error.exception.api.limitwithdaynum", String.valueOf(apiLimitCnt));
        }

        logger.debug(">liveDetect(), validateData= " + validateData + ", content length=" + content.length);
        LiveDetectForeResult result = tencentService.liveDetectFour(this.user.getUserId(), validateData, content);
        boolean isValid = result.isValid();
        this.response.isValid = isValid;
        String msg = "";
        if (result.getCompareMsg() != null) {
            msg += result.getCompareMsg();
        }
        if (result.getLiveMsg() != null) {
            if (msg != "") {
                msg += "\r\n";
            }
            msg += result.getLiveMsg();
        }
        this.response.msg = msg;
        // 插入日志
        userEventLogService.insertEventLog(userId, null, "user", "videoValidate", null, "1", this.getRemoteAddress());
        logger.debug("<liveDetect");

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private Boolean isValid;

        private String msg;

        private String validateData;

        public Boolean getIsValid() {
            return isValid;
        }

        public String getValidateData() {
            return validateData;
        }

        public String getMsg() {
            return msg;
        }

    }

}
