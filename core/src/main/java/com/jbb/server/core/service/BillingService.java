package com.jbb.server.core.service;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.server.core.domain.Billing;
import com.jbb.server.core.domain.BillingDetail;
import com.jbb.server.core.domain.Repayment;
import com.jbb.server.core.domain.UserBillingSummary;

public interface BillingService {
    int saveBilling(Billing billing);

    Billing getBilling(int userId, int billingId, boolean detail);

    List<Billing> getBillings(int userId, boolean detail);

    List<Billing> getBillingsByDate(int userId, Timestamp startDate, Timestamp endDate);

    void deleteBilling(int billingId);
    
    void updateBillingDetailStatus(int userId, int billingDetailId, int status);
    
    void saveRepayment(int userId, Repayment repayment);
    
    BillingDetail getBillingDetail( int userId, int billingDetailId);
    
    UserBillingSummary getBillingSummary(int userId);

}
