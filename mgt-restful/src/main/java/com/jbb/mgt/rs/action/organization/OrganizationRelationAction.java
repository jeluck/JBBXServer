package com.jbb.mgt.rs.action.organization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.OrganizationRelation;
import com.jbb.mgt.core.service.OrganizationRelationService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.mgt.server.rs.pojo.RsOrganization;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.exception.WrongParameterValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(OrganizationRelationAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OrganizationRelationAction extends BasicAction {

    public static final String ACTION_NAME = "OrganizationRelationAction";

    private static Logger logger = LoggerFactory.getLogger(OrganizationRelationAction.class);

    private OrganizationRelationAction.Response response;

    @Autowired
    OrganizationRelationService relationService;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new OrganizationRelationAction.Response();
    }

    /**
     * 新增组织和子组织关系
     * 
     * @param subOrgId
     */
    public void insertOrgRelation(Integer subOrgId) {
        logger.debug(">insertOrgRelation()");
        if (subOrgId == null || subOrgId == 0) {
            logger.debug("<insertOrgRelation() missing subOrgId");
            throw new MissingParameterException("jbb.error.exception.missing.param", "zh", "subOrgId");
        }
        if(subOrgId == this.account.getOrgId()){
            throw new WrongParameterValueException("jbb.mgt.error.exception.wrong.orgWithOneselfRelation");
        }
        List<OrganizationRelation> organizationRelations = relationService.selectOrgRelationByOrgId(this.account.getOrgId());
        if(organizationRelations.stream().anyMatch(s -> s.getSubOrgId().equals(subOrgId))){
            logger.debug("<insertOrgRelation() wrong subOrgId");
            throw new WrongParameterValueException("jbb.mgt.error.exception.wrong.orgRelationExist");
        }
        OrganizationRelation record = new OrganizationRelation(this.account.getOrgId(), subOrgId);
        relationService.insert(record);
        logger.debug("<insertOrgRelation()");
    }

    /**
     * 查询组织的关系信息
     */
    public void getOrgRelations() {
        logger.debug(">getOrgRelations()");
        this.response.orgRelations = relationService.selectOrgRelationByOrgId(this.account.getOrgId());
        logger.debug("<getOrgRelations()");
    }

    /**
     * 删除组织和子组织关系
     * 
     * @param subOrgId
     */
    public void delOrgRelation(Integer subOrgId) {
        logger.debug(">delOrgRelation()");
        OrganizationRelation record = new OrganizationRelation(this.account.getOrgId(), subOrgId);
        relationService.deleteByPrimaryKey(record);
        logger.debug("<delOrgRelation()");
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private List<OrganizationRelation> orgRelations;

        public List<OrganizationRelation> getOrgRelations() {
            return orgRelations;
        }
    }

}
