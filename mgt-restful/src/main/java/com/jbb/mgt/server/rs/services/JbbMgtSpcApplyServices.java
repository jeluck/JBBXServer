package com.jbb.mgt.server.rs.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.mgt.rs.action.spc.UserApplyRecordsSpcAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;

@Path("spc")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@NoCache
public class JbbMgtSpcApplyServices extends BasicRestfulServices {
    private static Logger logger = LoggerFactory.getLogger(JbbMgtSpcApplyServices.class);

    @GET
    @Path("/applies")
    public ActionResponse getApplies(@HeaderParam(API_KEY) String userKey, @QueryParam("accountId") Integer accountId,
        @QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate) {
        logger.debug(">getApplies()");
        UserApplyRecordsSpcAction action = getBean(UserApplyRecordsSpcAction.ACTION_NAME);
        action.validateUserKey(userKey);
        action.getApplies(accountId,startDate, endDate);
        logger.debug("<getApplies()");
        return action.getActionResponse();
    }
}
