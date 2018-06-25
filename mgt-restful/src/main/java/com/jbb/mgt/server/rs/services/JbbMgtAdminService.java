package com.jbb.mgt.server.rs.services;

import com.jbb.mgt.rs.action.Property.PropertyAction;
import com.jbb.mgt.rs.action.organization.OrganizationAction;
import com.jbb.mgt.rs.action.organization.OrganizationRelationAction;
import com.jbb.mgt.rs.action.user.UserApplyRecordAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.common.Constants;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * admin Services
 *
 * @author wyq
 * @date 2018/6/6 16:54
 */
@Path("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@NoCache
public class JbbMgtAdminService extends BasicRestfulServices {

    private static Logger logger = LoggerFactory.getLogger(JbbMgtAdminService.class);

    @GET
    @Path("/system/properties")
    public ActionResponse getPropertiesByName(@HeaderParam(API_KEY) String userKey, @QueryParam("name") String name) {
        logger.debug(">getPropertiesByName()");
        PropertyAction action = getBean(PropertyAction.ACTION_NAME);
        // 检查userKey
        action.validateUserKey(userKey);
        // 验证权限
        int[] ps = {Constants.MGT_P_SYSADMIN, Constants.MGT_P_ORGADMIN};
        action.validateUserAccess(ps);
        action.getPropertiesByName(name);
        logger.debug("<getPropertiesByName()");
        return action.getActionResponse();
    }

    @PUT
    @Path("/system/properties")
    public ActionResponse updatePropertiesByName(@HeaderParam(API_KEY) String userKey, @QueryParam("name") String name,
        @QueryParam("value") String value) {
        logger.debug(">updatePropertiesByName()");
        PropertyAction action = getBean(PropertyAction.ACTION_NAME);
        // 检查userKey
        action.validateUserKey(userKey);
        // 验证权限
        int[] ps = {Constants.MGT_P_SYSADMIN, Constants.MGT_P_ORGADMIN};
        action.validateUserAccess(ps);
        action.saveOrUpdatePropertiesByName(name, value);
        logger.debug("<updatePropertiesByName()");
        return action.getActionResponse();
    }

    @POST
    @Path("/org/relation")
    public ActionResponse insertOrgRelation(@HeaderParam(API_KEY) String userKey,
        @QueryParam("subOrgId") Integer subOrgId) {
        logger.debug(">insertOrgRelation()");
        OrganizationRelationAction action = getBean(OrganizationRelationAction.ACTION_NAME);
        // 检查userKey
        action.validateUserKey(userKey);
        // 验证权限
        int[] ps = {Constants.MGT_P_SYSADMIN};
        action.validateUserAccess(ps);
        action.insertOrgRelation(subOrgId);
        logger.debug("<insertOrgRelation()");
        return action.getActionResponse();
    }

    @PUT
    @Path("/org/relation")
    public ActionResponse delOrgRelation(@HeaderParam(API_KEY) String userKey,
        @QueryParam("subOrgId") Integer subOrgId) {
        logger.debug(">delOrgRelation()");
        OrganizationRelationAction action = getBean(OrganizationRelationAction.ACTION_NAME);
        // 检查userKey
        action.validateUserKey(userKey);
        // 验证权限
        int[] ps = {Constants.MGT_P_SYSADMIN};
        action.validateUserAccess(ps);
        action.delOrgRelation(subOrgId);
        logger.debug("<delOrgRelation()");
        return action.getActionResponse();
    }

    @GET
    @Path("/org/relation")
    public ActionResponse getOrgRelation(@HeaderParam(API_KEY) String userKey) {
        logger.debug(">getOrgRelation()");
        OrganizationRelationAction action = getBean(OrganizationRelationAction.ACTION_NAME);
        // 检查userKey
        action.validateUserKey(userKey);
        // 验证权限
        int[] ps = {Constants.MGT_P_SYSADMIN, Constants.MGT_P_ORGADMIN};
        action.validateUserAccess(ps);
        action.getOrgRelations();
        logger.debug("<getOrgRelation()");
        return action.getActionResponse();
    }

    @GET
    @Path("/organizations")
    public ActionResponse getOrganizations(@HeaderParam(API_KEY) String userKey,
        @QueryParam("getStatistic") Boolean getStatistic) {
        logger.debug(">getOrganizations()");
        OrganizationAction action = getBean(OrganizationAction.ACTION_NAME);
        // 检查userKey
        action.validateUserKey(userKey);
        // 验证权限
        int[] ps = {Constants.MGT_P_SYSADMIN, Constants.MGT_P_ORGADMIN,Constants.MGT_P_MGTCHANNEL};
        action.validateUserAccess(ps);
        action.getOrganizations(getStatistic);
        logger.debug("<getOrganizations()");
        return action.getActionResponse();
    }

    @GET
    @Path("/organizations/jbbusers")
    public ActionResponse selectUserApplyRecords(@HeaderParam(API_KEY) String userKey,
        @QueryParam("orgId") Integer orgId, @QueryParam("startDate") String startDate,
        @QueryParam("endDate") String endDate) {
        logger.debug(">selectUserApplyRecords()");
        UserApplyRecordAction action = getBean(UserApplyRecordAction.ACTION_NAME);
        // 检查userKey
        action.validateUserKey(userKey);
        // 验证权限
        int[] ps = {Constants.MGT_P_SYSADMIN, Constants.MGT_P_ORGADMIN};
        action.validateUserAccess(ps);
        action.selectUserApplyRecords(orgId, startDate, endDate);
        logger.debug("<getOrganizations()");
        return action.getActionResponse();
    }
}
