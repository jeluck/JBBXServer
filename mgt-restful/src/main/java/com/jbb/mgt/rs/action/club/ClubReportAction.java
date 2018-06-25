package com.jbb.mgt.rs.action.club;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.ClubReport;
import com.jbb.mgt.core.service.ClubService;
import com.jbb.mgt.rs.action.channel.ChannelAction;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.common.util.StringUtil;

@Service(ClubReportAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ClubReportAction extends BasicAction {

    public static final String ACTION_NAME = "ClubReportAction";

    private static Logger logger = LoggerFactory.getLogger(ChannelAction.class);

    @Autowired
    private ClubService clubService;

    private Response response;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    public void notifyToGetAndSaveReport(Integer userId, String taskId) {

        logger.debug(">notifyToGetAndSaveReport,userId =" + userId + ", taskId =" + taskId);

        if (StringUtil.isEmpty(taskId) || userId == null) {
            logger.info("notifyToGetAndSaveReport, taskId or userId is empty");
            return;
        }

        clubService.saveReport(userId, taskId);
        logger.debug("<notifyToGetAndSaveReport");
    }

    public void getClubReport(int userId, String channelType) {

        logger.debug(">getClubReport,userId =" + userId + ", channelType =" + channelType);
        // TODO 检查用户是否和此机构有申请
        
        this.response.report = clubService.getReport(userId, channelType);
        logger.debug("<getClubReport");
    }

    public void getClubReport(String taskId) {

        logger.debug(">getClubReport,taskId =" + taskId);

        this.response.report = clubService.getReport(taskId);
        logger.debug("<getClubReport");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        ClubReport report;

        public ClubReport getReport() {
            return report;
        }

    }
}
