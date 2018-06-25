package com.jbb.server.rs.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.core.domain.Iou;
import com.jbb.server.core.domain.UserBillingSummary;
import com.jbb.server.core.service.BillingService;
import com.jbb.server.core.service.IousService;
import com.jbb.server.rs.pojo.ActionResponse;
import com.jbb.server.rs.pojo.RsIou;

@Service(UserTotalAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserTotalAction extends BasicAction {

    private static Logger logger = LoggerFactory.getLogger(UserTotalAction.class);

    public static final String ACTION_NAME = "UserTotalAction";

    private Response response;

    @Autowired
    private BillingService billingService;

    @Autowired
    private IousService iousService;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    public void getTotal() {
        getBillingSummary();
//        getRecentIous();
    }

    private void getBillingSummary() {
        logger.debug(">getBillingSummary(),userId =" + this.user.getUserId());
        this.response.userBillingSummay = billingService.getBillingSummary(this.user.getUserId());
        logger.debug("<getBillingSummary()");
    }
    
     //获取最近的借条信息，暂时不用
    private void getRecentIous() {
        logger.debug(">getRecentIous(),userId =" + this.user.getUserId());
        List<Iou> ious = iousService.getRecentIousForBorrower(this.user.getUserId());
        ious.forEach(iou -> iou.prepareCalFields());
        this.response.recentIous = ious;
        logger.debug("<getRecentIous()");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {

        private UserBillingSummary userBillingSummay;

        private List<Iou> recentIous;

        public UserBillingSummary getUserBillingSummay() {
            return userBillingSummay;
        }

        public List<Iou> getRecentIous() {
            return recentIous;
        }

    }

}
