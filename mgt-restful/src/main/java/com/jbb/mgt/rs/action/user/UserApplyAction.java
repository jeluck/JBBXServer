package com.jbb.mgt.rs.action.user;

import java.sql.Timestamp;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.common.constant.AccountOpType;
import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.core.domain.UserApplyRecord;
import com.jbb.mgt.core.service.AccountService;
import com.jbb.mgt.core.service.UserApplyRecordService;
import com.jbb.mgt.core.service.UserService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.mgt.server.rs.pojo.RsUser;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.shared.rs.Util;

/**
 * 借款用户申请Action
 * 
 * @author jarome
 * @date 2018/4/29
 */
@Service(UserApplyAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserApplyAction extends BasicAction {

    public static final String ACTION_NAME = "UserApplyAction";

    private static Logger logger = LoggerFactory.getLogger(UserApplyAction.class);

    private Response response;

    @Autowired
    @Qualifier("AccountService")
    private AccountService accountService;
    @Autowired
    @Qualifier("UserApplyRecordService")
    private UserApplyRecordService userApplyRecordService;

    @Autowired
    private UserService userService;

    @Override
    protected ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    public void getUserDetailInfo(int applyId) {

    	this.response.user = userService.selectUserByApplyId(applyId);

    }

    /**
     * 分配用户
     * 
     * @param request
     */
    public void assignUser(Request request) {
        logger.debug(">assignUser", request);
        // 判断是否传入userIds
        // if () {
        // throw new WrongParameterValueException("Wrong UserIds");
        // }
        // // 判断传入的id是否正确
        // verifyAccountId(request.accountId);
        // /* int finAccId = 0;
        // int [] down = accountService.selectdownAccounts(request.accountId);
        //
        // if(down.length==0) {
        // finAccId=request.accountId;
        // }else {
        // finAccId=down[0];
        // int index=(int)(Math.random()*down.length);
        // finAccId=down[index];
        // System.out.println(finAccId);
        // }*/
        // userApplyRecordService.assignAccount(this.account.getOrgId(),
        // this.account.getAccountId(),request.accountId,
        // Arrays.asList(request.userIds));
        logger.debug("<assignUser", request);
    }

    /**
     * 审批处理
     * 
     * @param applyId
     * @param request
     */
    public void applyApprove(Integer applyId, Request request) {
        logger.debug(">apply,applyId=" + applyId, request);
        verifyApplyParam(request);
        // 更新apply
        // userApplyRecordService.updateUserApplyForApprove(applyId,
        // request.opType, this.account.getAccountId(),
        // request.accountId, request.amount, request.mark, request.reason,
        // request.comment);
        logger.debug("<apply,applyId=" + applyId, request);
    }

    public void countUserApplyRecords(String channelCode, String startDate, String endDate) {
        logger.debug(">countUserApplyRecords");
        if (StringUtil.isEmpty(channelCode)) {
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "channelCode");
        }
        TimeZone tz = getTimezone();
        Timestamp tsStartDate = null;
        Timestamp tsEndDate = null;
        if (!StringUtil.isEmpty(startDate)) {
            tsStartDate = Util.parseTimestamp(startDate, tz);
        }
        if (!StringUtil.isEmpty(endDate)) {
            tsEndDate = Util.parseTimestamp(endDate, tz);
        }
        this.response.count =
            userApplyRecordService.countUserApplyRecords(this.account.getOrgId(), channelCode, tsStartDate, tsEndDate);
        this.response.sum =
            userApplyRecordService.countUserApplyRecords(this.account.getOrgId(), channelCode, null, null);
        logger.debug("<countUserApplyRecords");
    }

    private void verifyApplyParam(Request request) {
        if (request.opType == null) {
            throw new MissingParameterException("Missing opType");
        }
        // if (request.opType.equals(AccountOpType.INIT) ||
        // request.opType.equals(AccountOpType.FINAL)) {
        // if (request.accountId == null) {
        // throw new MissingParameterException("Missing accountId");
        // }
        // }
        if (request.amount == null || request.amount <= 0) {
            throw new WrongParameterValueException("Wrong amount");
        }
        if (StringUtils.isBlank(request.reason)) {
            throw new WrongParameterValueException("Wrong reason");
        }
        if (StringUtils.isBlank(request.comment)) {
            throw new WrongParameterValueException("Wrong comment");
        }
    }

    private void verifyAccountId(Integer accountId) {
        boolean res = accountService.verifyAccountId(accountId, this.account.getOrgId());
        if (!res) {
            throw new WrongParameterValueException("Wrong AccountId");
        }
    }

    public static class AssignRequest {
        public int[] userIds;
        public int[] accountIds;
        public boolean isAllApplyUsers;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Request {
        public AccountOpType opType;
        /**
         * 操作金额，系统金额都为整数，单位分
         */
        public Integer amount;
        /**
         * 操作理由
         */
        public String reason;
        /**
         * 操作日志
         */
        public String comment;
        /**
         * 审核标记
         */
        public String mark;
        public Integer orgId;
        public Integer timeType;

    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {

        public User user;

        public Integer count;
        public Integer sum;

        public Integer getSum() {
            return sum;
        }

        public Integer getCount() {
            return count;
        }

    }
}
