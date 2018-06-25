package com.jbb.mgt.rs.action.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.service.UserApplyRecordService;
import com.jbb.mgt.rs.action.index.IndexCountAction;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.shared.rs.Util;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.TimeZone;

/**
 * Created by Administrator on 2018/5/19.
 */

@Service(UserApplyStatisticAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserApplyStatisticAction extends BasicAction {

    public static final String ACTION_NAME = "UserApplyStatisticAction";

    private static Logger logger = LoggerFactory.getLogger(UserApplyStatisticAction.class);

    private Response response;

    @Autowired
    private UserApplyRecordService userApplyRecordService;

    @Override
    protected ActionResponse makeActionResponse() {
        return this.response = new UserApplyStatisticAction.Response();
    }

    /**
     * 查询统计数据
     * 
     * @param statuses
     * @param startDate
     * @param endDate
     */
    public void getStatisticsNumber(Integer[] statuses, String startDate, String endDate) {
        logger.debug(">getStatisticsNumber statuses={}, startDate{} endDate{}", statuses, startDate, endDate);
        if (ArrayUtils.isEmpty(statuses)) {
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "statuses");
        }
        Timestamp tsStartDate = Util.parseTimestamp(startDate, TimeZone.getDefault());
        Timestamp tsEndDate = Util.parseTimestamp(endDate, TimeZone.getDefault());
        this.response.count = userApplyRecordService.getStatisticsNumber(statuses, tsStartDate, tsEndDate);
        this.response.totalCount = userApplyRecordService.getStatisticsNumber(statuses, null, null);
        logger.debug("<getStatisticsNumber");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        public Integer count;
        public Integer totalCount;

        public Integer getTotalCount() {
            return totalCount;
        }

        public Integer getCount() {
            return count;
        }

    }

}
