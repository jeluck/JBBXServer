package com.jbb.server.rs.action;

import java.sql.Timestamp;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.common.Constants;
import com.jbb.server.core.domain.IousAmountStatistic;
import com.jbb.server.core.service.IousService;
import com.jbb.server.rs.pojo.ActionResponse;
import com.jbb.server.shared.rs.Util;

@Service(IousStatisticAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class IousStatisticAction extends BasicAction {

    private static Logger logger = LoggerFactory.getLogger(IousStatisticAction.class);

    public static final String ACTION_NAME = "IousStatitsticAction";

    private Response response;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    @Autowired
    private IousService iousService;

    /**
     * 借条中心
     * 
     * @param userId
     * @param type
     */
    public void statistic(String startDate, String endDate, Integer statisticType, Integer type) {
        logger.debug(">statistic , startDate=" + startDate + " , endDate=" + endDate);
        int userId = this.user.getUserId();
        TimeZone tz = getTimezone();
        Timestamp tsStartDate = Util.parseTimestamp(startDate, tz);
        Timestamp tsEndDate = Util.parseTimestamp(endDate, tz);

        if (statisticType == null || Constants.IOU_STATISTIC_EFFECT_WITHOUT_COMPLETED == statisticType) {

            if (type == null) {
                // 统计借入借出
                this.response.borrowerStatistic = iousService.statisticIousAmountAndCnt(
                    Constants.IOU_STATUS_EFFECT_WITHOUT_COMPLETED, userId, null, tsStartDate, tsEndDate);
                this.response.lenderStatistic = iousService.statisticIousAmountAndCnt(
                    Constants.IOU_STATUS_EFFECT_WITHOUT_COMPLETED, null, userId, tsStartDate, tsEndDate);
            } else if (type == 1) {
                // 统计借入
                this.response.borrowerStatistic = iousService.statisticIousAmountAndCnt(
                    Constants.IOU_STATUS_EFFECT_WITHOUT_COMPLETED, userId, null, tsStartDate, tsEndDate);
            } else if (type == 2) {
                // 统计借出
                this.response.lenderStatistic = iousService.statisticIousAmountAndCnt(
                    Constants.IOU_STATUS_EFFECT_WITHOUT_COMPLETED, null, userId, tsStartDate, tsEndDate);
            }
        } else if (Constants.IOU_STATISTIC_EFFECT == statisticType) {
            if (type == null) {
                this.response.borrowerStatistic = iousService.statisticIousAmountAndCnt(Constants.IOU_STATUS_EFFECT,
                    userId, null, tsStartDate, tsEndDate);
                this.response.lenderStatistic = iousService.statisticIousAmountAndCnt(Constants.IOU_STATUS_EFFECT, null,
                    userId, tsStartDate, tsEndDate);
            } else if (type == 1) {
                // 统计借入
                this.response.borrowerStatistic = iousService.statisticIousAmountAndCnt(Constants.IOU_STATUS_EFFECT,
                    userId, null, tsStartDate, tsEndDate);
            } else if (type == 2) {
                // 统计借出
                this.response.lenderStatistic = iousService.statisticIousAmountAndCnt(Constants.IOU_STATUS_EFFECT, null,
                    userId, tsStartDate, tsEndDate);
            }
        }

        logger.debug("<statistic");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {

        private IousAmountStatistic borrowerStatistic;
        private IousAmountStatistic lenderStatistic;

        public IousAmountStatistic getBorrowerStatistic() {
            return borrowerStatistic;
        }

        public IousAmountStatistic getLenderStatistic() {
            return lenderStatistic;
        }

    }
}
