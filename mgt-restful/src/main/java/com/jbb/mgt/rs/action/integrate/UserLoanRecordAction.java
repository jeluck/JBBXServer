package com.jbb.mgt.rs.action.integrate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.Account;
import com.jbb.mgt.core.domain.LoanOpLog;
import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.core.domain.UserLoanRecord;
import com.jbb.mgt.core.service.AccountService;
import com.jbb.mgt.core.service.LoanRecordOpLogService;
import com.jbb.mgt.core.service.UserLoanRecordService;
import com.jbb.mgt.core.service.UserService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.server.common.exception.ObjectNotFoundException;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.shared.rs.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

/**
 * JBB打借条action
 *
 * @author wyq
 * @date 2018/5/29 20:49
 */
@Component(UserLoanRecordAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserLoanRecordAction extends BasicAction {

    public static final String ACTION_NAME = "UserLoanRecordAction";

    private static Logger logger = LoggerFactory.getLogger(UserLoanRecordAction.class);

    @Autowired
    private UserLoanRecordService userLoanRecordService;

    @Autowired
    private LoanRecordOpLogService loanRecordOpLogService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    public void createUserLoanRecord(Request req) {
        logger.debug(">createUserLoanRecord, UserLoanRecord={}", req);
        UserLoanRecord record = generateUserLoanRecord(req);
        Account account = accountService.selectAccountByJbbUserId(req.lenderId);
        if (null == account){
            throw new ObjectNotFoundException("jbb.error.exception.ioufill.userNotExist","zh","lenderId");
        }
        User user = userService.selectJbbUserById(req.borrowerId,account.getOrgId());
        if (null == user){
            throw new ObjectNotFoundException("jbb.error.exception.ioufill.userNotExist","zh","borrowerId");
        }
        record.setAccountId(account.getAccountId());
        record.setUserId(user.getUserId());
        record.setStatus(-1);
        record.setCreationDate(DateUtil.getCurrentTimeStamp());
        record.setIouPlatformId(1);
        userLoanRecordService.createUserLoanRecord(record);
        LoanOpLog loanOpLog = new LoanOpLog();
        loanOpLog.setOpType(100);
        loanRecordOpLogService.insertOpLog(loanOpLog);
        logger.debug("<createUserLoanRecord");
    }

    private UserLoanRecord generateUserLoanRecord(Request req) {
        UserLoanRecord ulr = new UserLoanRecord();
        ulr.setIouCode(req.iouCode);
        TimeZone tz = getTimezone();
        ulr.setBorrowingDate(Util.parseTimestamp(req.borrowingDate, tz));
        ulr.setBorrowingAmount(req.borrowingAmount);
        ulr.setRepaymentDate(Util.parseTimestamp(req.repaymentDate, tz));
        ulr.setAnnualRate(req.annualRate);
        ulr.setIouStatus(req.status);
        return ulr;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Request {
        public String iouCode;
        public String borrowingDate;
        public Integer borrowingAmount;
        public String repaymentDate;
        public Integer annualRate;
        public Integer lenderId;
        public Integer borrowerId;
        public Integer status;
    }

}
