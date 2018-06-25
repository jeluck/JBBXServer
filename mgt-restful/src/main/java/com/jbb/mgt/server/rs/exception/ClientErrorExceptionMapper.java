package com.jbb.mgt.server.rs.exception;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.mgt.server.rs.pojo.ApiResponse;
import com.jbb.server.shared.rs.InformationCodes;

@Provider
public class ClientErrorExceptionMapper extends BaseExceptionMapper implements ExceptionMapper<ClientErrorException> {
    private static Logger logger = LoggerFactory.getLogger(ClientErrorExceptionMapper.class);

    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(ClientErrorException ex) {
        String method = null;
        String uri = null;
        String requestHeaders = null;

        if (request != null) {
            uri = request.getRequestURI();
            method = request.getMethod();

            if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("\n  Request headers:");
                sb.append("\n  Accept=").append(request.getHeader("Accept"));
                sb.append("\n  Content-Type=").append(request.getHeader("Content-Type"));
                sb.append("\n  User-Agent=").append(request.getHeader("User-Agent"));
                sb.append("\n  Range=").append(request.getHeader("Range"));
                sb.append("\n  RemoteHost=").append(request.getRemoteHost());
                sb.append("\n  RemoteAddr=").append(request.getRemoteAddr());
                sb.append("\n  Query=").append(request.getQueryString());

                requestHeaders = sb.toString();
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug(
                "Client error on calling API method " + method + " " + uri + " : " + ex.toString() + requestHeaders);
        }

        ActionResponse response = new ActionResponse();
        String exMessage = ex.getMessage();
        if ((exMessage != null) && (exMessage.indexOf("Unable to extract parameter") >= 0)) {
            /*
             * Exception message example: 
             * RESTEASY003870: Unable to extract parameter from http request:
             * javax.ws.rs.QueryParam("duration") value is {1} for
             * public com.jbb.greenxserver.rs.pojo.ActionResponse
             * com.jbb.mgt.server.rs.services.CloudServicesJson.postCircleFile(
             * java.lang.String,java.lang.String,int,java.lang.Integer,java.lang.String,
             * java.lang.String,java.lang.Integer,java.lang.Short,boolean,java.lang.Integer,byte[])
             */
            int ind = exMessage.indexOf(" for");
            if (ind > 0)
                exMessage = exMessage.substring(0, ind);
            response.setResultCodeAndMessage(InformationCodes.WRONG_PARAM_VALUE,
                exMessage + " in API method " + method + " " + uri);
        } else {
            response.setResultCodeAndMessage(InformationCodes.METHOD_NOT_FOUND,
                "API method " + method + " " + uri + " not found");
        }

        if (request != null) {
            request.setAttribute(ApiResponse.API_RESPONSE, response);
        }

        return makeMapperResponse(uri, response);
    }
}
