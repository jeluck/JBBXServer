package com.jbb.mgt.rs.action.loanRecord;

import java.util.List;

import com.jbb.server.common.exception.WrongParameterValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.LoanOpLog;
import com.jbb.mgt.core.service.LoanRecordOpLogService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;

@Service(LoanOpLogAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LoanOpLogAction extends BasicAction {

    public static final String ACTION_NAME = "LoanOpLogAction";

    private static Logger logger = LoggerFactory.getLogger(LoanOpLogAction.class);

    private Response response;

    @Autowired
    LoanRecordOpLogService loanRecordOpLogService;

    @Override
    protected ActionResponse makeActionResponse() {
        return this.response = new LoanOpLogAction.Response();
    }

    /**
     * 新增操作记录
     * 
     * @param LoanId
     * @param req
     */
    public void insertLoanOpLog(Integer LoanId, Request req) {
        if (null == LoanId || LoanId == 0) {
            throw new WrongParameterValueException("jbb.mgt.error.exception.missing.param", "zh", "loanId");
        }
        // 记录操作日志
        LoanOpLog opLog = new LoanOpLog(LoanId, req.opType == null ? 0 : req.opType, this.account.getAccountId(),
            req.comment, req.reason);
        loanRecordOpLogService.insertOpLog(opLog);
    }

    /**
     * 查询操作记录
     * 
     * @param loanId
     */
    public void getLoanOpLogs(Integer loanId,Integer[] opTypes) {
        if (null == loanId || loanId == 0) {
            throw new WrongParameterValueException("jbb.mgt.error.exception.missing.param", "zh", "loanId");
        }
        this.response.opLogs = loanRecordOpLogService.selectLoanRecordOpLogByLoanId(loanId,opTypes);
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private List<LoanOpLog> opLogs;

        public List<LoanOpLog> getOpLogs() {
            return opLogs;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Request {
        public Integer opType;
        public String reason;
        public String comment;
    }

}
