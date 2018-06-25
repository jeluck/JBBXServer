package com.jbb.server.rs.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.rs.action.WxPayNotifyAction;

@Path("xml")
@Produces(MediaType.TEXT_XML)
@Consumes({MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@NoCache
public class JbbServicesXml extends BasicRestfulServices {

    private static Logger logger = LoggerFactory.getLogger(JbbServicesXml.class);

    @POST
    @Path("/wxpay/notifyPayResult")
    public String notifyPayResult(@Context HttpServletRequest request) {
        logger.debug(">notifyPayResult(), request= " + request);
        WxPayNotifyAction action = getBean(WxPayNotifyAction.ACTION_NAME);
        String result = action.payNotify(request);
        logger.debug("<notifyPayResult()");
        return result;
    }
}
