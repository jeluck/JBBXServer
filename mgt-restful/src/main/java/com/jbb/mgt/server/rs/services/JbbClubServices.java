package com.jbb.mgt.server.rs.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.mgt.rs.action.account.AccountAction;
import com.jbb.mgt.rs.action.club.ClubNotifyAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.common.Constants;

/**
 * API Services
 * 
 * @author VincentTang
 * @date 2017年12月20日
 */

@Path("mgt")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
@NoCache
public class JbbClubServices extends BasicRestfulServices {

    private static Logger logger = LoggerFactory.getLogger(JbbClubServices.class);

    @POST
    @Path("/club/notify")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public String clubNotify(@FormParam("notify_event") String event, @FormParam("notify_type") String type,
        @FormParam("notify_time") String time, @FormParam("notify_data") String data, @FormParam("sign") String sign,
        @FormParam("passback_params") String params) {
        logger.debug(">clubNotify(), event = " + event + ", type=" + type + ", data=" + data + ", time=" + time
            + ", sign=" + sign + ", params=" + params);
        ClubNotifyAction action = getBean(ClubNotifyAction.ACTION_NAME);
        action.notify(event, type, time, data, sign, params);
        logger.debug("<clubNotify()");
        return "{\"message\": \"回调处理成功\", \"code\": 0}";
    }

}
