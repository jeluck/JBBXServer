package com.jbb.mgt.rs.action.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.UserApplyRecord;
import com.jbb.mgt.core.service.UserApplyRecordService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.mgt.server.rs.pojo.RsUserApplyRecord;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.shared.rs.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * userApplyRecord action类
 * 
 * @author wyq
 * @date 2018/6/9 17:52
 */
@Service(UserApplyRecordAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserApplyRecordAction extends BasicAction {

    public static final String ACTION_NAME = "UserApplyRecordAction";

    private static Logger logger = LoggerFactory.getLogger(UserApplyRecordAction.class);

    private Response response;

    @Autowired
    private UserApplyRecordService userApplyRecordService;

    @Override
    protected ActionResponse makeActionResponse() {
        return this.response = new UserApplyRecordAction.Response();
    }

    public void selectUserApplyRecords(Integer orgId, String startDate, String endDate) {
        logger.debug(">selectUserApplyRecords,orgId=", orgId + "startDate=" + startDate + "endDate=" + endDate);
        if (null == orgId || orgId == 0) {
            throw new WrongParameterValueException("jbb.error.exception.wrong.paramvalue", "orgId");
        }
        if (StringUtil.isEmpty(startDate)) {
            startDate = null;
        }
        if (StringUtil.isEmpty(endDate)) {
            endDate = null;
        }
        List<UserApplyRecord> list = userApplyRecordService.selectUserApplyRecordsByOrgId(orgId,
            Util.parseTimestamp(startDate, getTimezone()), Util.parseTimestamp(endDate, getTimezone()));
        if (list.size() > 0) {
            this.response.users = new ArrayList<RsUserApplyRecord>(list.size());
            for (UserApplyRecord u : list) {
                this.response.users.add(new RsUserApplyRecord(u));
            }
        }
        logger.debug("<selectUserApplyRecords");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Request {

    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private List<RsUserApplyRecord> users;

        public List<RsUserApplyRecord> getUsers() {
            return users;
        }
    }
}
