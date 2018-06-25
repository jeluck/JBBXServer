package com.jbb.server.rs.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.rs.action.ClubAuthorizationAction;
import com.jbb.server.rs.pojo.ActionResponse;

/**
 * club Services
 * 
 * @author xieshile
 * @date 2018年4月10日
 */

@Path("club")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@NoCache
public class ClubServices extends BasicRestfulServices {

    private static Logger logger = LoggerFactory.getLogger(ClubServices.class);

    @GET
    @Path("/authorization")
    public ActionResponse getAuthorization(@QueryParam("realName") String realName,
    		@QueryParam("identityCode") String identityCode,@QueryParam("userMobile") String userMobile,
    		@QueryParam("userPass") String userPass) {
        logger.debug(">getAuthorization() ");
        ClubAuthorizationAction action = getBean(ClubAuthorizationAction.ACTION_NAME);
        action.Authorization(realName, identityCode, userMobile, userPass);
        logger.debug("<getAuthorization()");
        return action.getActionResponse();
    }

 
}
