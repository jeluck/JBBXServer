package com.jbb.server.rs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.core.domain.BillingDetail;
import com.jbb.server.core.service.BillingService;
import com.jbb.server.rs.pojo.ActionResponse;
import com.jbb.server.rs.pojo.request.ReRepayment;

@Service(BillingDetailAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BillingDetailAction extends BasicAction {
    private static Logger logger = LoggerFactory.getLogger(BillingAction.class);

    public static final String ACTION_NAME = "BillingDetailAction";

    private Response response;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    @Autowired
    private BillingService billingService;

    public void updateStatus(int billingId, int billingDetailId, int status) {
        billingService.updateBillingDetailStatus(this.user.getUserId(), billingDetailId, status);
    }

    public void savePayment(ReRepayment request) {
        billingService.saveRepayment(this.user.getUserId(), request.prepareRepayment());
    }
    
    public void getBillingDetail(int billingDetailId){
        this.response.billingDetail =  billingService.getBillingDetail(this.user.getUserId(), billingDetailId);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private BillingDetail billingDetail;

        public BillingDetail getBillingDetail() {
            return billingDetail;
        }
        
    }
}
