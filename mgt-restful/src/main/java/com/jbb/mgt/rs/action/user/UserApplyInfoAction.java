
package com.jbb.mgt.rs.action.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.UserApplyRecord;
import com.jbb.mgt.core.service.UserApplyRecordService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;

@Service(UserApplyInfoAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserApplyInfoAction extends BasicAction {

    public static final String ACTION_NAME = "UserApplyInfoAction";

    private static Logger logger = LoggerFactory.getLogger(UserApplyListAction.class);

    @Autowired
    private UserApplyRecordService userApplyRecordService;

    private Response response;
    
    @Override
    protected ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    public void getUserApplyRecord(int applyId) {
        logger.debug("<getUserApplyRecord(), applyId=" + applyId);

        this.response.userApplyRecord =
            userApplyRecordService.getUserApplyRecordByApplyId(applyId, null, null, this.account.getOrgId());

        logger.debug("<getUserApplyRecord()");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {

        private UserApplyRecord userApplyRecord;

        public UserApplyRecord getUserApplyRecord() {
            return userApplyRecord;
        }

    }

}
