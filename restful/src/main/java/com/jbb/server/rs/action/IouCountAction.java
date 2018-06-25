package com.jbb.server.rs.action;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.common.Constants;
import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.core.domain.UserProperty;
import com.jbb.server.core.service.IousService;
import com.jbb.server.core.service.UserLoginLogsService;
import com.jbb.server.rs.action.LoginAction.Response;
import com.jbb.server.rs.pojo.ActionResponse;

@Service(IouCountAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class IouCountAction extends BasicAction {

    private static Logger logger = LoggerFactory.getLogger(IouCountAction.class);

    public static final String ACTION_NAME = "IouCountAction";

    private Response response;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    @Autowired
    private IousService iousService;

    public void count(Integer category) {
        logger.debug(">count(), category =" + category);
        // 无用户登录状态，直接返回
        if (this.user == null) {
            this.response.hallCnt = countIous(Constants.USER_P_READAT_HALL);
            return;
        }

        this.response.hallCnt = countIous(Constants.USER_P_READAT_HALL);
        this.response.lendCnt = countIous(Constants.USER_P_READAT_LEND);
        this.response.publishCnt = countIous(Constants.USER_P_READAT_PUBLISH);
        this.response.followCnt = countIous(Constants.USER_P_READAT_FOLLOW);

        logger.debug("<count()");
    }

    private Timestamp parseStartTime(Long ts) {
        if (ts != null) {
            return new Timestamp(ts);
        }
        return null;
    }

    private int countIous(String name) {
        Timestamp start = null;
        if (this.user != null) {
            Long readAt = this.coreAccountService.getLongProperty(this.user.getUserId(), name);
            start = parseStartTime(readAt);
        }

        int cnt = 0;
        switch (name) {
            case Constants.USER_P_READAT_HALL:
                cnt = iousService.countHallNewIous(start);
                break;
            case Constants.USER_P_READAT_LEND:
                cnt = iousService.countBorrowOrLendUpdatedIous(null, this.user.getUserId(), start);
                break;
            case Constants.USER_P_READAT_PUBLISH:
                cnt = iousService.countBorrowOrLendUpdatedIous(this.user.getUserId(), null, start);
                break;
            case Constants.USER_P_READAT_FOLLOW:
                cnt = iousService.countFollowUpdatedIous(this.user.getUserId(), start);
                break;
        }
        return cnt;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {

        private int hallCnt;
        private int lendCnt;
        private int publishCnt;
        private int followCnt;

        public int getHallCnt() {
            return hallCnt;
        }

        public int getLendCnt() {
            return lendCnt;
        }

        public int getPublishCnt() {
            return publishCnt;
        }

        public int getFollowCnt() {
            return followCnt;
        }

    }
}