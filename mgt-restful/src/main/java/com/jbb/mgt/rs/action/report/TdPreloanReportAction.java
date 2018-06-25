
package com.jbb.mgt.rs.action.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.PreloanReport;
import com.jbb.mgt.core.service.TdRiskService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.common.util.StringUtil;

@Service(TdPreloanReportAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TdPreloanReportAction extends BasicAction {
    public static final String ACTION_NAME = "TdPreloanReportAction";

    private static Logger logger = LoggerFactory.getLogger(TdPreloanReportAction.class);

    private Response response;

    @Autowired
    private TdRiskService tdRiskService;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    public void getPreloanReport(Integer applyId, String reportId, Integer userId) {
        logger.debug(">getPreloanReport() applyId ={}, reportId={}, userId={}", applyId, reportId, userId);

        if (!StringUtil.isEmpty(reportId)) {
            this.response.report = tdRiskService.getPreloanReportByReportId(reportId);
        } else if (applyId != null) {
            //暂时关闭通过applyId查询 。 目前通过用户ID查询，避免多次查询的情况发生，引起多次费用。
            this.response.report = null;
        } else if (userId != null) {
            this.response.report = tdRiskService.getPreloanReportByUserId(userId);
        }

        logger.debug("<getPreloanReport");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        PreloanReport report;

        public PreloanReport getReport() {
            return report;
        }

    }

}
