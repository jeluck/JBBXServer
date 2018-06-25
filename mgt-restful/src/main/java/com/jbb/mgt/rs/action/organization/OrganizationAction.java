package com.jbb.mgt.rs.action.organization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.Organization;
import com.jbb.mgt.core.service.OrganizationService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.common.exception.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 组织管理Action类
 *
 * @author wyq
 * @date 2018/6/6 17:16
 */
@Service(OrganizationAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrganizationAction extends BasicAction {
    public static final String ACTION_NAME = "OrganizationAction";

    private static Logger logger = LoggerFactory.getLogger(OrganizationAction.class);

    private Response response;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new OrganizationAction.Response();
    }

    @Autowired
    private OrganizationService organizationService;

    public void getOrganizations(Boolean getStatistic) {
        logger.debug(">getOrganizations");
        if (this.account.getOrgId() != 1) {
            throw new AccessDeniedException("jbb.error.validateAdminAccess.accessDenied");
        }
        Boolean isGetStatistic = getStatistic == null ? false : getStatistic;
        List<Organization> list = organizationService.getAllOrganizations(isGetStatistic);
        this.response.organizations = list;
        logger.debug(">getOrganizations");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {

        private List<Organization> organizations;

        public List<Organization> getOrganizations() {
            return organizations;
        }
    }

}
