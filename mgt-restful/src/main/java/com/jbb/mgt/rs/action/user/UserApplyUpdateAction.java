
package com.jbb.mgt.rs.action.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.AccountOpLog;
import com.jbb.mgt.core.domain.UserApplyRecord;
import com.jbb.mgt.core.service.AccountOpLogService;
import com.jbb.mgt.core.service.UserApplyRecordService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.common.Constants;
import com.jbb.server.common.util.DateUtil;

import io.netty.util.internal.StringUtil;

@Service(UserApplyUpdateAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserApplyUpdateAction extends BasicAction {

    public static final String ACTION_NAME = "UserApplyUpdateAction";

    private static Logger logger = LoggerFactory.getLogger(UserApplyListAction.class);

    @Autowired
    private UserApplyRecordService userApplyRecordService;

    @Autowired
    private AccountOpLogService accountOpLogService;

    public void updateUserApplyRecord(int applyId, Request req) {
        logger.debug("<updateUserApplyRecord(), applyId=" + applyId + ", req=" + req);

        UserApplyRecord record =
            userApplyRecordService.getUserApplyRecordByApplyId(applyId, null, null, this.account.getOrgId());

        boolean isChanged = convert2Update(req, record);
        if (isChanged) {

            userApplyRecordService.updateUserAppayRecord(record);
            // 记录操作日志
            if(req.opType !=null){
                AccountOpLog opLog = new AccountOpLog(applyId, req.opType == null ? 0 : req.opType,
                    this.account.getAccountId(), req.commet, req.reason);
                accountOpLogService.createOpLog(opLog);
            }
           
        }

        logger.debug("<updateUserApplyRecord()");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {

    }

    private boolean convert2Update(Request req, UserApplyRecord record) {
        boolean isChanged = false;
        if (req.status != null) {
            record.setStatus(req.status);
            if(req.status == 6){
                record.setInitDate(DateUtil.getCurrentTimeStamp());
            }else if (req.status == 10){
                record.setFinalDate(DateUtil.getCurrentTimeStamp());
            }else if(req.status==3||req.status==7){
                record.setRejectDate(DateUtil.getCurrentTimeStamp());
            }else if (req.status==4||req.status==8) {
                record.setHangupDate(DateUtil.getCurrentTimeStamp());
            }
            isChanged = true;
        }
        if (req.assignAccId != null) {
            int[] ps = {Constants.MGT_P_ORGADMIN};// 没有确定权限
            this.validateUserAccess(ps);
            record.setAssignAccId(req.assignAccId);
            record.setAssingDate(DateUtil.getCurrentTimeStamp());
            isChanged = true;
        }
        if (req.initAccId != null) {
            int[] ps = {Constants.MGT_P_ASSIGN, Constants.MGT_P_ORGADMIN, Constants.MGT_P_PRE_CHECK};// 没有确定权限
            this.validateUserAccess(ps);
            record.setInitAccId(req.initAccId);
            isChanged = true;
        }

        if (req.initAmount != null) {
            int[] ps = {Constants.MGT_P_PRE_CHECK, Constants.MGT_P_ORGADMIN};// 没有确定权限
            this.validateUserAccess(ps);

            record.setInitAmount(req.initAmount);
            isChanged = true;
        }

        if (req.finalAccId != null) {
            int[] ps = {Constants.MGT_P_PRE_CHECK, Constants.MGT_P_ORGADMIN, Constants.MGT_P_FIN_CHECK};// 没有确定权限
            this.validateUserAccess(ps);
            record.setFinalAccId(req.finalAccId);
            isChanged = true;
        }

        if (req.finalAmount != null) {
            int[] ps = {Constants.MGT_P_FIN_CHECK, Constants.MGT_P_ORGADMIN};// 没有确定权限
            this.validateUserAccess(ps);
            record.setFinalAmount(req.finalAmount);
            isChanged = true;
        }

        if (req.loanAccId != null) {
            int[] ps = {Constants.MGT_P_FIN_CHECK, Constants.MGT_P_ORGADMIN};// 没有确定权限
            this.validateUserAccess(ps);
            record.setLoanAccId(req.loanAccId);
            record.setLoanDate(DateUtil.getCurrentTimeStamp());
            isChanged = true;
        }
        if (req.loanAmount != null) {
            int[] ps = {Constants.MGT_P_LOAN_MGT, Constants.MGT_P_ORGADMIN};// 没有确定权限
            this.validateUserAccess(ps);
            record.setLoanAmount(req.loanAmount);
            isChanged = true;
        }
        if (!StringUtil.isNullOrEmpty(req.initMark)) {
            int[] ps = {Constants.MGT_P_PRE_CHECK, Constants.MGT_P_ORGADMIN};// 没有确定权限
            this.validateUserAccess(ps);
            record.setInitMark(generateMarkStr(record.getInitMark(), req.initMark));
            isChanged = true;
        }
        if (!StringUtil.isNullOrEmpty(req.finalMark)) {
            int[] ps = {Constants.MGT_P_FIN_CHECK, Constants.MGT_P_ORGADMIN};// 没有确定权限
            this.validateUserAccess(ps);
            record.setFinalMark(generateMarkStr(record.getFinalMark(), req.finalMark));
            isChanged = true;
        }
        return isChanged;
    }

    private String generateMarkStr(String orginMark, String newMark) {
        if (StringUtil.isNullOrEmpty(orginMark)) {
            return newMark;
        } else {
            return orginMark + "\r\n" + newMark;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Request {

        public Integer status;
        public Integer assignAccId;
        public Integer initAccId;
        public Integer initAmount;
        public Integer finalAccId;
        public Integer finalAmount;
        public Integer loanAccId;
        public Integer loanAmount;
        public String initMark;
        public String finalMark;

        // 操作日志
        public Integer opType;
        public String reason;
        public String commet;

    }

}
