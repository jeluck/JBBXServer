package com.jbb.mgt.rs.action.count;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.jbb.mgt.core.domain.OrgRecharges;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.Bill;
import com.jbb.mgt.core.domain.StatisticsMoney;
import com.jbb.mgt.core.service.AccountOpLogService;
import com.jbb.mgt.core.service.LoanRecordOpLogService;
import com.jbb.mgt.core.service.OrgRechargesService;
import com.jbb.mgt.core.service.UserApplyRecordService;
import com.jbb.mgt.core.service.UserService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.shared.rs.Util;

@Service(CountAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CountAction extends BasicAction {

    public static final String ACTION_NAME = "CountAction";

    private static Logger logger = LoggerFactory.getLogger(CountAction.class);

    private Response response;

    @Autowired
    @Qualifier("UserService")
    private UserService userService;
    @Autowired
    @Qualifier("UserApplyRecordService")
    private UserApplyRecordService userApplyRecordService;
    @Autowired
    private AccountOpLogService accountOpLogService;
    @Autowired
    private LoanRecordOpLogService loanRecordOpLogService;

    @Override
    protected ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    public void getPushCount(Integer type) {
        logger.debug(">getPushCount");
        Integer orgId = this.account.getOrgId();
        this.response.count = userService.getPushCount(orgId, type);
        logger.debug("<getPushCount");
    }

    public void getDiversionCount(Integer type) {
        logger.debug(">getPushCount");
        Integer orgId = this.account.getOrgId();
        this.response.count = userApplyRecordService.getDiversionCount(orgId, type);
        logger.debug("<getPushCount");

    }

    public void getCount(Integer[] ops, Boolean type) {
        Integer accountId = this.account.getAccountId();
        Boolean newtype = type == null ? false : type;
        this.response.count = accountOpLogService.getCount(accountId, ops, newtype);
        logger.debug("<getCount");
    }

    public void getmoneyCount(Integer op, Boolean type) {
        Integer accountId = this.account.getAccountId();
        Boolean newtype = type == null ? false : type;
        this.response.count = loanRecordOpLogService.getmoneyCount(accountId, op, newtype);
        logger.debug("<getmoneyCount");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        public Integer count;
    }

}
