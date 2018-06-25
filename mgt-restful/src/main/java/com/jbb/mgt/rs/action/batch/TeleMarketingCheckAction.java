package com.jbb.mgt.rs.action.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.jbb.mgt.core.domain.OrgRecharges;
import com.jbb.mgt.core.domain.TeleMarketing;
import com.jbb.mgt.core.service.AccountService;
import com.jbb.mgt.core.service.OrgRechargeDetailService;
import com.jbb.mgt.core.service.OrgRechargesService;
import com.jbb.mgt.core.service.TeleMarketingService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.WrongParameterValueException;

@Service(TeleMarketingCheckAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TeleMarketingCheckAction extends BasicAction {

    public static final String ACTION_NAME = "TeleMarketingCheckAction";

    private static Logger logger = LoggerFactory.getLogger(TeleMarketingCheckAction.class);
    private static DefaultTransactionDefinition NEW_TX_DEFINITION
        = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    int unitPrice = PropertyManager.getIntProperty("jbb.mgt.phonecheck.price", 5);

    @Autowired
    private TeleMarketingService marketingService;
    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    private AccountService accountService;
    @Autowired
    private OrgRechargesService orgRechargesService;
    @Autowired
    private OrgRechargeDetailService orgRechargeDetailService;

    public void updateTeleMarketing(Integer batchId) {
        TransactionStatus txStatus = null;
        try {
            txStatus = txManager.getTransaction(NEW_TX_DEFINITION);
            TeleMarketing teleMarketing = marketingService.selectTeleMarkByBatchId(batchId,false);
            if (teleMarketing == null) {
                throw new WrongParameterValueException("jbb.mgt.exception.batchIdNotFound");
            }
            if (teleMarketing.getStatus() == 3) {
                throw new WrongParameterValueException("jbb.mgt.exception.overPayment");
            }
            teleMarketing.setStatus(3);
            marketingService.updateTeleMarketing(teleMarketing);
            int orgId = accountService.getAccountById(teleMarketing.getAccountId(), null, false).getOrgId();
            OrgRecharges o = orgRechargesService.selectOrgRechargesForUpdate(orgId);
            if (o == null) {
                throw new WrongParameterValueException("jbb.mgt.exception.smsAmount.notEnough2");
            }
            int totalSmsExpense = o.getTotalSmsExpense() + teleMarketing.getCnt() * unitPrice;
            if (totalSmsExpense > o.getTotalSmsAmount()) {
                throw new WrongParameterValueException("jbb.mgt.exception.smsAmount.notEnough2");
            }
            o.setTotalSmsExpense(totalSmsExpense);
            orgRechargeDetailService.consumePhoneNumberCheck(teleMarketing);
            orgRechargesService.updateOrgRecharges(o);
            txManager.commit(txStatus);
            txStatus = null;
        } finally {
            // roll back not committed transaction (release user lock)
            rollbackTransaction(txStatus);
        }
    }

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
}
