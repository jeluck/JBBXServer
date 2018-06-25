package com.jbb.server.core.dao;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.server.core.domain.Billing;
import com.jbb.server.core.domain.BillingDetail;
import com.jbb.server.core.domain.Repayment;

public interface BillingDao {

    // 账单表操作
    void createBilling(Billing billing);

    void deleteBilling(int billingId);

    void updateBilling(Billing billing);

    Billing getBilling(int userId, int billingId, boolean detail);

    List<Billing> getBillings(int userId, boolean detail);
    
    List<Billing> getBillingsByDate(int userId, Timestamp startDate, Timestamp endDate);

    // 账单明细记录操作
    void createBillingDetail(BillingDetail billingDetail);

    void updateBillingDetail(BillingDetail billingDetail);

    BillingDetail getBillingDetail(int billingDetailId, boolean detail);

    List<BillingDetail> getBillingDetails(int billingId, boolean detail);

    // 还款记录
    void createRepayment(Repayment repayment);

    void deleteRepayment(int repaymentId);

    Repayment getRepayment(int repaymentId);

    List<Repayment> getRepayments(int billingDetailId);

}
