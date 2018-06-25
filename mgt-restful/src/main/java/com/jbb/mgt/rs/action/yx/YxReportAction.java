package com.jbb.mgt.rs.action.yx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.YxNotify;
import com.jbb.mgt.core.domain.YxReport;
import com.jbb.mgt.core.service.YxReportService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.StringUtil;

@Service(YxReportAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class YxReportAction extends BasicAction {
    public static final String ACTION_NAME = "YxReportAction";

    private static Logger logger = LoggerFactory.getLogger(YxReportAction.class);

    private Response response;

    @Autowired
    private YxReportService yxReportService;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    public void generateH5Url(int userId, int applyId, String reportType) {
        logger.debug(">generateH5Url(), userId = " + userId + " ,applyId=" + applyId + " ,reportType=" + reportType);
        this.response.h5Url = yxReportService.generateH5Url(userId, applyId, reportType);
        logger.debug("<generateH5Url()");
    }

    public void notify(YxNotify notify) {
        logger.debug(">notify(), notify = " + notify);
        yxReportService.notify(notify);
        logger.debug("<notify()");

    }

    public void getReport(Integer userId, Integer applyId, String taskId, String reportType) {
        logger.debug(">getReport(), userId = " + userId + " ,applyId=" + applyId + " ,taskId=" + taskId
            + " ,reportType=" + reportType);
        this.response.report = yxReportService.getReport(userId, applyId, taskId, reportType);
        logger.debug("<getReport()");

    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {

        private String h5Url;

        private YxReport report;

        public String getH5Url() {
            return h5Url;
        }

        public YxReport getReport() {
            return report;
        }

    }

}
