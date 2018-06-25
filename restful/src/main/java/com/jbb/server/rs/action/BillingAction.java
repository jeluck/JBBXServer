
package com.jbb.server.rs.action;

import java.sql.Timestamp;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.ObjectNotFoundException;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.domain.Billing;
import com.jbb.server.core.service.BillingService;
import com.jbb.server.rs.pojo.ActionResponse;
import com.jbb.server.rs.pojo.request.ReBilling;
import com.jbb.server.shared.rs.Util;

@Service(BillingAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BillingAction extends BasicAction {

    private static Logger logger = LoggerFactory.getLogger(BillingAction.class);

    public static final String ACTION_NAME = "BillingAction";

    private Response response;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    @Autowired
    private BillingService billingService;

    public void getBillings(String startDate, String endDate, Boolean detail) {
        if (logger.isDebugEnabled()) {
            logger.debug(">getBillings(), startDate=" +  startDate + ", endDate=" + endDate + ", detail=" + detail);
        }
        TimeZone tz = getTimezone();
        Timestamp tsStartDate = Util.parseTimestamp(startDate, tz);
        Timestamp tsEndDate = Util.parseTimestamp(endDate, tz);

        long currentTime = DateUtil.getCurrentTime();

        if (tsStartDate.after(tsEndDate)) {
            logger.debug("<getBillings() wrong dates: startDate={}, endDate={}", tsStartDate, tsEndDate);
            return;
        }
        this.response.pictureServerUrl = PropertyManager.getProperty("jbb.picture.server.address");
        this.response.path = PropertyManager.getProperty("jbb.picture.server.loanpic.path");
        this.response.billings = billingService.getBillingsByDate(this.user.getUserId(), tsStartDate, tsEndDate);
        logger.debug("<getBillings");
    }

    public void createBilling(ReBilling reBilling) {
        if (logger.isDebugEnabled()) {
            logger.debug(">createBilling(), billing={}", reBilling);
        }
        Billing billing = reBilling.generateBilling();
        billing.setUserId(this.user.getUserId());
        Integer billingId = billingService.saveBilling(billing);
        this.response.billingId = billingId;
        logger.debug("<createBilling");
    }

    public void updateBilling(int billingId, ReBilling reBilling) {
        if (logger.isDebugEnabled()) {
            logger.debug(">updateBilling(), billingId ={}, billing={}", billingId, reBilling);
        }
        // check billing is belongs this user
        getBilling(this.user.getUserId(), billingId, false);
        // end get and check
        Billing billing = reBilling.generateBilling();
        billing.setBillingId(billingId);
        billing.setUserId(this.user.getUserId());
        billingService.saveBilling(billing);
        logger.debug("<updateBilling");
    }

    public void deleteBilling(int billingId) {
        // check billing is belongs this user
        getBilling(this.user.getUserId(), billingId, false);
        // end get and check
        billingService.deleteBilling(billingId);
    }

    public void getBilling(int billingId, boolean detail) {
        this.response.pictureServerUrl = PropertyManager.getProperty("jbb.picture.server.address");
        this.response.path = PropertyManager.getProperty("jbb.picture.server.loanpic.path");
        this.response.billing = getBilling(this.user.getUserId(), billingId, detail);
    }

    public Billing getBilling(int userId, int billingId, boolean detail) {
        Billing billing = billingService.getBilling(userId, billingId, detail);
        if (billing == null) {
            throw new ObjectNotFoundException("jbb.error.exception.billing.notFound");
        }
        return billing;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        
        private String pictureServerUrl;
        private String path;
  
        private Integer billingId;  
        private Billing billing;
        private List<Billing> billings;
        
        public String getPictureServerUrl() {
            return pictureServerUrl;
        }
        
        public String getPath() {
            return path;
        }

        public Integer getBillingId() {
            return billingId;
        }

        public Billing getBilling() {
            return billing;
        }

        public List<Billing> getBillings() {
            return billings;
        }

    }
}
