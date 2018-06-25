package com.jbb.mgt.rs.action.msgCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.jbb.mgt.core.domain.Channel;
import com.jbb.mgt.core.domain.OrgRecharges;
import com.jbb.mgt.core.domain.Organization;
import com.jbb.mgt.core.service.AccountService;
import com.jbb.mgt.core.service.AliyunService;
import com.jbb.mgt.core.service.ChannelService;
import com.jbb.mgt.core.service.ChuangLanService;
import com.jbb.mgt.core.service.OrgRechargeDetailService;
import com.jbb.mgt.core.service.OrgRechargesService;
import com.jbb.mgt.core.service.OrganizationService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.WrongParameterValueException;

@Component(MsgCodeAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MsgCodeAction extends BasicAction {
    private static Logger logger = LoggerFactory.getLogger(MsgCodeAction.class);
    public static final String ACTION_NAME = "msgCodeAction";
    private static DefaultTransactionDefinition NEW_TX_DEFINITION
        = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

    @Autowired
    private AliyunService aliyunService;
    @Autowired
    private OrgRechargesService orgRechargesService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrgRechargeDetailService orgRechargeDetailService;
    @Autowired
    private ChuangLanService chuangLanService;

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

    public void sendCode(String phoneNumber, String channelCode) {
        logger.debug(">sendMsgCode(), phoneNumber=" + phoneNumber + " , channelCode=" + channelCode);
        int smsPrice = PropertyManager.getIntProperty("jbb.mgt.sms.price", 10);
        TransactionStatus txStatus = null;
        try {
            txStatus = txManager.getTransaction(NEW_TX_DEFINITION);
            Channel channel = channelService.getChannelByCode(channelCode);
            if (channel == null || channel.getStatus() == 1 || channel.getStatus() == 2) {
                throw new WrongParameterValueException("jbb.mgt.error.exception.channelNotFound");
            }
            int orgId = accountService.getAccountById(channel.getCreator(), null, false).getOrgId();
            OrgRecharges o = orgRechargesService.selectOrgRechargesForUpdate(orgId);
            if (o == null || o.getTotalSmsAmount() - o.getTotalSmsExpense() < smsPrice) {
                throw new WrongParameterValueException("jbb.mgt.exception.maxMsgSend");
            }
            o.setTotalSmsExpense(o.getTotalSmsExpense() + smsPrice);
            orgRechargesService.updateOrgRecharges(o);
            Organization org = organizationService.getOrganizationById(orgId);
            orgRechargeDetailService.handleConsumeSms(channelCode, phoneNumber, smsPrice);
            // aliyunService.sendCode(phoneNumber, channelCode, org.getSmsSignName(), org.getSmsTemplateId());
            chuangLanService.sendMsgCode(phoneNumber, channelCode, org.getSmsSignName());

            txManager.commit(txStatus);
            txStatus = null;
        } finally {
            // roll back not committed transaction (release user lock)
            rollbackTransaction(txStatus);
        }
        logger.debug("<sendMsgCode()");
    }

}
