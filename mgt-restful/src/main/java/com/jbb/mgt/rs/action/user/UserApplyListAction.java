
package com.jbb.mgt.rs.action.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.jbb.mgt.core.domain.UserApplyRecord;
import com.jbb.mgt.core.service.UserApplyRecordService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;

@Service(UserApplyListAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserApplyListAction extends BasicAction {

    public static final String ACTION_NAME = "UserApplyListAction";

    private static Logger logger = LoggerFactory.getLogger(UserApplyListAction.class);

    private Response response;

    @Autowired
    private UserApplyRecordService userApplyRecordService;

    @Override
    protected ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    public void getUserApplyRecords(int pageNo, int pageSize, String orderBy, Boolean desc, String op, int[] statuses,
        String phoneNumberSearch, String channelSearch, String usernameSearch, String assignNameSearch,
        String initNameSearch, String finalNameSearch, String loanNameSearch, String idcardSearch,
        String jbbIdSearch) {
        logger.debug(">getUserApplyRecords(), pageNo=" + pageNo + "pageSize=" + pageSize + "statuses=" + statuses
            + "phoneNumberSearch=" + phoneNumberSearch + "channelSearch=" + channelSearch + "usernameSearch="
            + usernameSearch + "op=" + op + "op=" + initNameSearch + "initNameSearch=" + initNameSearch
            + "assignNameSearch=" + assignNameSearch + "finalNameSearch=" + finalNameSearch + "loanNameSearch="
            + loanNameSearch + "idcardSearch=" + idcardSearch + "jbbIdSearch=" + jbbIdSearch);

        PageHelper.startPage(pageNo, pageSize);
        if (!StringUtil.isEmpty(orderBy)) {
            String order = (desc != null && desc == true) ? "desc" : "asc";
            PageHelper.orderBy("c." + orderBy + " " + order);
        }
        Integer accountId = null;
        if(!this.isOrgAdmin()){
            accountId = this.account.getAccountId();
        }
        List<UserApplyRecord> list = userApplyRecordService.getUserApplyRecords(null, op, accountId, this.account.getOrgId(), statuses, phoneNumberSearch, channelSearch, usernameSearch, assignNameSearch,
            initNameSearch, finalNameSearch, loanNameSearch, idcardSearch, jbbIdSearch);
        PageInfo<UserApplyRecord> pageInfo = new PageInfo<UserApplyRecord>(list);
        // this.response.userApplyRecoreds = list;
        this.response.pageInfo = pageInfo;
        
        PageHelper.clearPage();
        logger.debug("<getUserApplyRecords()");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private PageInfo<UserApplyRecord> pageInfo;

        public PageInfo<UserApplyRecord> getPageInfo() {
            return pageInfo;
        }

    }

}
