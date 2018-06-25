package com.jbb.server.core.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.dao.BillingDao;
import com.jbb.server.core.domain.Billing;
import com.jbb.server.core.domain.BillingDetail;
import com.jbb.server.core.domain.Repayment;
import com.jbb.server.core.domain.UserBillingSummary;
import com.jbb.server.core.service.BillingService;

@Service("billingService")
public class BillingServiceImpl implements BillingService {
    private static Logger logger = LoggerFactory.getLogger(BillingServiceImpl.class);

    private static int BILLING_TYPE_ONCE = 1;

    private static int RETURN_RECENT_BILLINGS_SIZE = 3;

    private static DefaultTransactionDefinition NEW_TX_DEFINITION =
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

    @Autowired
    private BillingDao billingDao;

    @Autowired
    private PlatformTransactionManager txManager;

    private void rollbackTransaction(TransactionStatus txStatus) {
        if (txStatus == null) {
            return;
        }
        try {
            txManager.rollback(txStatus);
        } catch (Exception er) {
            logger.warn("Cannot rollback transaction", er);
        }
    }

    @Override
    public int saveBilling(Billing billing) {
        TransactionStatus txStatus = null;
        try {
            txStatus = txManager.getTransaction(NEW_TX_DEFINITION);
            if (billing.getBillingId() != null) {
                // update
                billingDao.updateBilling(billing);
                updateBillingOnceDetail(billing);
            } else {
                // create
                billingDao.createBilling(billing);
                // update billing details
                createBillingDetails(billing);
            }

            txManager.commit(txStatus);
            txStatus = null;
        } finally {
            rollbackTransaction(txStatus);
        }
        return billing.getBillingId();

    }

    private void createBillingDetails(Billing billing) {
        if (BILLING_TYPE_ONCE == billing.getLoanTypeId()) {
            // create billing detail
            createBillingOnceDetail(billing);
        }
    }

    private void createBillingOnceDetail(Billing billing) {
        BillingDetail bd = new BillingDetail();
        bd.setBillingId(billing.getBillingId());
        bd.setBalance(billing.getpAmount());
        bd.setCurrentNo(1);
        bd.setReturnDate(billing.getpDate());
        billingDao.createBillingDetail(bd);
    }

    private void calculateBillingDetailStatus(BillingDetail bd) {
        if (bd.getBalance() <= bd.getPaymentSum()) {
            bd.setStatus(1);
        } else {
            bd.setStatus(0);
        }
    }

    private void updateBillingOnceDetail(Billing billing) {
        List<BillingDetail> bdList = billingDao.getBillingDetails(billing.getBillingId(), false);
        for (BillingDetail bd : bdList) {
            bd.setBalance(billing.getpAmount());
            bd.setReturnDate(billing.getpDate());

            calculateBillingDetailStatus(bd);

            billingDao.updateBillingDetail(bd);
        }
    }

    @Override
    public void deleteBilling(int billingId) {
        billingDao.deleteBilling(billingId);
    }

    @Override
    public Billing getBilling(int userId, int billingId, boolean detail) {
        return billingDao.getBilling(userId, billingId, detail);
    }

    @Override
    public List<Billing> getBillings(int userId, boolean detail) {
        return billingDao.getBillings(userId, detail);
    }

    @Override
    public List<Billing> getBillingsByDate(int userId, Timestamp startDate, Timestamp endDate) {
        return billingDao.getBillingsByDate(userId, startDate, endDate);
    }

    @Override
    public void updateBillingDetailStatus(int userId, int billingDetailId, int status) {
        BillingDetail bd = billingDao.getBillingDetail(billingDetailId, false);
        bd.setStatus(status);
        billingDao.updateBillingDetail(bd);
    }

    @Override
    public void saveRepayment(int userId, Repayment repayment) {
        TransactionStatus txStatus = null;
        try {
            txStatus = txManager.getTransaction(NEW_TX_DEFINITION);
            billingDao.createRepayment(repayment);
            int bDetailId = repayment.getBillingDetailId();
            // BillingDetail bd = billingDao.getBillingDetail(bDetailId, false);
            List<Repayment> list = billingDao.getRepayments(bDetailId);
            Double amount = 0.0;
            Timestamp ts = null;
            for (Repayment item : list) {
                amount += item.getAmount();
                if (ts == null || ts.before(item.getRepayDate())) {
                    ts = item.getRepayDate();
                }
            }
            BillingDetail bd = billingDao.getBillingDetail(bDetailId, false);
            bd.setPaymentSum(amount);
            bd.setLastPaymentDate(ts);
            calculateBillingDetailStatus(bd);
            billingDao.updateBillingDetail(bd);
            txManager.commit(txStatus);
            txStatus = null;
        } finally {
            rollbackTransaction(txStatus);
        }

    }

    @Override
    public UserBillingSummary getBillingSummary(int userId) {
        // TODO 后续改进为单独获取每项数据
        List<Billing> billings = billingDao.getBillings(userId, true);
        double totalBorrow = 0.0;
        double totalBalance = 0.0;
        double totalRepayment = 0.0;
        double totalInterest = 0.0;

        Timestamp overdueBillingTs = null;
        Timestamp recentBillingTs = null;
        Billing overdueBilling = null;
        Billing recentBilling = null;

        long todyTs = DateUtil.getTodayStartTs();

        int size = billings.size();
        List<Billing> notReturnedBillings = new ArrayList<Billing>();

        for (int index = 0; index < size; index++) {
            Billing b = billings.get(index);
            List<BillingDetail> bdList = b.getDetails();

            totalBorrow += b.getbAmount();
            totalBalance += b.getpAmount();

            boolean isReturned = true;
            for (BillingDetail bd : bdList) {
                totalRepayment += bd.getPaymentSum();
                isReturned = isReturned && (bd.getStatus() == 1);
            }
            
            if (isReturned) {
                continue;
            }

            if (b.getpDate().getTime() < todyTs && (overdueBillingTs == null || b.getpDate().after(overdueBillingTs))) {
                overdueBillingTs = b.getpDate();
                overdueBilling = b;
            }
            if (b.getpDate().getTime() >= todyTs && (recentBillingTs == null || b.getpDate().before(recentBillingTs))) {
                recentBillingTs = b.getpDate();
                recentBilling = b;
            }

            if (!isReturned) {
                notReturnedBillings.add(b);
            }
        }

        // 返回recentBillings
        List<Billing> recentBillings = new ArrayList<Billing>();
        int len = notReturnedBillings.size();
        if (len > 0) {
            // 排序
            notReturnedBillings.sort(new Comparator<Billing>() {
                public int compare(Billing o1, Billing o2) {
                    return o1.getpDate().before(o2.getpDate()) ? 1 : -1;
                }
            });
            int indexF = 0;
            for (int i = 0; i < len; i++) {
                if (notReturnedBillings.get(i).getpDate().getTime() >= todyTs) {
                    indexF = i;
                    break;
                }
            }
            if((indexF + RETURN_RECENT_BILLINGS_SIZE)<=len){
                recentBillings.add(notReturnedBillings.get(indexF));
                recentBillings.add(notReturnedBillings.get(indexF+1));
                recentBillings.add(notReturnedBillings.get(indexF+2));
              
            }else if(len<=RETURN_RECENT_BILLINGS_SIZE){
                recentBillings.addAll(notReturnedBillings);
            }else{
                recentBillings.add(notReturnedBillings.get(len-3));
                recentBillings.add(notReturnedBillings.get(len-2));
                recentBillings.add(notReturnedBillings.get(len-1));
            }
        }

        // end recentBillings
        totalInterest = totalBalance - totalBorrow;
        UserBillingSummary summary = new UserBillingSummary(totalBorrow, totalBalance, totalRepayment, totalInterest,
            overdueBilling, recentBilling);
        summary.setRecentBillings(recentBillings);
        return summary;
    }

    @Override
    public BillingDetail getBillingDetail(int userId, int billingDetailId) {
        return billingDao.getBillingDetail(billingDetailId, false);
    }

}
