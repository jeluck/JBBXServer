package com.jbb.server.rs.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.domain.LoanPlatformPolicy;
import com.jbb.server.core.domain.LenderReason;
import com.jbb.server.rs.action.ChannelAction;
import com.jbb.server.rs.action.ChannelPolicyAction;
import com.jbb.server.rs.action.RecommandUserAction;
import com.jbb.server.rs.action.StatisticUserAction;
import com.jbb.server.rs.pojo.ActionResponse;
import com.jbb.server.rs.pojo.request.ReChannel;

@Path("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
@NoCache
public class JbbAdminServices extends BasicRestfulServices {

    private static Logger logger = LoggerFactory.getLogger(JbbAdminServices.class);

    @GET
    @Path("/users/recommand")
    public ActionResponse recommandUsers(@HeaderParam(API_KEY) String userKey,
        @QueryParam("startDate") Long startDateTs, @QueryParam("endDate") Long endDateTs,
        @QueryParam("userId") Integer userId) {
        logger
            .debug(">recommandUsers(), startDate = " + startDateTs + " , endDate=" + endDateTs + " , userId=" + userId);
        RecommandUserAction action = getBean(RecommandUserAction.ACTION_NAME);
        action.validateUserKey(userKey);
        action.validateUserAccess();
        action.getRecommandUsers(startDateTs, endDateTs, userId);
        logger.debug("<recommandUsers()");
        return action.getActionResponse();
    }

    @GET
    @Path("/users/statistic")
    public ActionResponse statisticUsers(@HeaderParam(API_KEY) String userKey, @QueryParam("dateStr") String dateStr,
        @QueryParam("userId") Integer userId) {
        logger.debug(">statisticUsers(), dateStr = " + dateStr + " , userId=" + userId);
        StatisticUserAction action = getBean(StatisticUserAction.ACTION_NAME);
        action.validateUserKey(userKey);
        action.validateStaticticAccess();
        action.statisticUserBySourceId(dateStr, userId);
        logger.debug("<statisticUsers()");
        return action.getActionResponse();
    }

    @GET
    @Path("/channels")
    public ActionResponse getChannels(@HeaderParam(API_KEY) String userKey,
        @QueryParam("channelType") Integer channelType) {
        logger.debug(">getChannels()");
        ChannelAction action = getBean(ChannelAction.ACTION_NAME);
        action.validateUserKey(userKey);
        action.validateSystemAdminPermission();
        action.getChannels(channelType);
        logger.debug("<getChannels()");
        return action.getActionResponse();
    }

    @POST
    @Path("/channel")
    public ActionResponse registerChannel(@HeaderParam(API_KEY) String userKey,
        @QueryParam("phoneNumber") String phoneNumber, @QueryParam("username") String username,
        @HeaderParam("password") String password, @QueryParam("sourceIds") String sourceIds,
        @QueryParam("roleId") Integer roleId,  ReChannel req) {
        logger.debug(">registerChannel()");
        ChannelAction action = getBean(ChannelAction.ACTION_NAME);
        action.validateUserKey(userKey);
        action.validateSystemAdminPermission();
        action.registerChannel(phoneNumber, username, password, roleId, sourceIds,  req);
        logger.debug("<registerChannel()");
        return action.getActionResponse();
    }
    
    @PUT
    @Path("/channel")
    public ActionResponse updateChannel(@HeaderParam(API_KEY) String userKey,
        @QueryParam("phoneNumber") String phoneNumber, @QueryParam("username") String username,
        @QueryParam("sourceIds") String sourceIds,@HeaderParam("password") String password,
        @QueryParam("roleId") Integer roleId, ReChannel req) {
        logger.debug(">updateChannel()");
        ChannelAction action = getBean(ChannelAction.ACTION_NAME);
        action.validateUserKey(userKey);
        action.validateSystemAdminPermission();
        action.updateChannelUser(phoneNumber, username, password, roleId, sourceIds, req);
        logger.debug("<updateChannel()");
        return action.getActionResponse();
    }

    @GET
    @Path("/channel/policy")
    public ActionResponse getChannelPolicy(@HeaderParam(API_KEY) String userKey) {
        logger.debug(">getChannelPolicy()");
        ChannelPolicyAction action = getBean(ChannelPolicyAction.ACTION_NAME);
        action.validateUserKey(userKey);
        action.validateSystemAdminPermission();
        action.getPolicy();
        logger.debug("<getChannelPolicy()");
        return action.getActionResponse();
    }

    @POST
    @Path("/channel/policy")
    public ActionResponse registerChannelPolicy(@HeaderParam(API_KEY) String userKey, LoanPlatformPolicy policy) {
        logger.debug(">upudateChannelPolicy(), policy= " + policy.toString());
        ChannelPolicyAction action = getBean(ChannelPolicyAction.ACTION_NAME);
        action.validateUserKey(userKey);
        action.validateSystemAdminPermission();
        action.updatePolicy(policy);
        logger.debug("<upudateChannelPolicy()");
        return action.getActionResponse();
    }
    
    @POST
    @Path("/channel/reason/{phoneNumber}")
    public ActionResponse postReason(@HeaderParam(API_KEY) String userKey, 
        @PathParam("phoneNumber") String phoneNumber, LenderReason reason) {
        logger.debug(">postReason(), phoneNumber= " + phoneNumber +" , reason="+ reason);
        ChannelAction action = getBean(ChannelAction.ACTION_NAME);
        action.validateUserKey(userKey);
        action.validateUserAccess();
        action.updateReason(phoneNumber, reason);
        logger.debug("<postReason()");
        return action.getActionResponse();
    }
    
}