package com.jbb.server.core.dao.mapper;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.Billing;
import com.jbb.server.core.domain.BillingDetail;
import com.jbb.server.core.domain.Repayment;

public interface BillingMapper {
    
    /**
     * 插入账单
     * 
     * @param billing
     */
    int insertBilling(Billing billing);
    
    /**
     * 删除账单
     * 
     * @param billingId
     */
    void deleteBilling(@Param("billingId")  int billingId);

    /**
     * 更新账单
     * 
     * @param billing
     */
    void updateBilling(Billing billing);

    /**
     * 选择账单
     * 
     * @param billing
     */
    Billing selectBilling(@Param("userId") int userId, @Param("billingId") int billingId, @Param("detail") boolean detail);

    /**
     * 获取账单列表
     * 
     * @param userId
     * @param detail
     */
    List<Billing> selectBillings(@Param("userId")int userId, @Param("detail") boolean detail);
    
    /**
     * 获取账单列表
     * 
     * @param userId
     * @param detail
     */
    List<Billing> selectBillingsByDate(@Param("userId")int userId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);
    

    /**
     * 插入账单名细
     * 
     * @param billingDetail
     */
    void insertBillingDetail(BillingDetail billingDetail);

    /**
     * 更新账单名细
     * 
     * @param billingDetail
     */
    void updateBillingDetail(BillingDetail billingDetail);

    /**
     * 选择账单名细
     * 
     * @param billingDetail
     */
    BillingDetail selectBillingDetail(@Param("billingDetailId") int billingDetailId, @Param("detail")  boolean detail);

    /**
     * 获取账单的账单明细列表
     * 
     * @param billingDetail
     */
    List<BillingDetail> selectBillingDetails(@Param("billingId") int billingId, @Param("detail")boolean detail);

    /**
     * 插入还款记录
     * 
     * @param repayment
     */
    void insertRepayment(Repayment repayment);

    /**
     * 删除还款记录
     * 
     * @param repaymentId
     */
    void deleteRepayment(@Param("repaymentId") int repaymentId);

    /**
     * 获取还款记录
     * 
     * @param repaymentId 还款记录ID
     * @return
     */
    Repayment selectRepayment(@Param("repaymentId") int repaymentId);

    /**
     * 获取账单的还款记录
     * 
     * @param billingDetailId 账单明细ID
     * @return
     */
    List<Repayment> selectRepayments(@Param("billingDetailId") int billingDetailId);
}
