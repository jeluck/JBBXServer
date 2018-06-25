package com.jbb.server.rs.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.rs.action.intergrate.MobileAuthAction;
import com.jbb.server.rs.pojo.ActionResponse;

@Path("integrate")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@NoCache
public class JbbIntegrateService extends BasicRestfulServices {
    private static Logger logger = LoggerFactory.getLogger(JbbIntegrateService.class);
    @POST
    @Path("/auth/mobile")
    public ActionResponse notifyValidateMobile(@HeaderParam(API_KEY) String apiKey,
        @QueryParam("phoneNumber") String phoneNumber, @QueryParam("userId") int userId) {
        logger.debug(">notifyValidateMobile() phoneNumber="+phoneNumber +", userId="+userId);
        MobileAuthAction action = getBean(MobileAuthAction.ACTION_NAME);
        action.validateMgtPlatformApiKey(apiKey);
        action.authMobile(phoneNumber, userId);
        logger.debug("<notifyValidateMobile()");
        return action.getActionResponse();
    }

}