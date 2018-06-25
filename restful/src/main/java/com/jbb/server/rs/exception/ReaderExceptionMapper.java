package com.jbb.server.rs.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.ReaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.jbb.server.rs.pojo.ActionResponse;
import com.jbb.server.rs.pojo.ApiResponse;
import com.jbb.server.shared.rs.InformationCodes;


@Provider
public class ReaderExceptionMapper extends BaseExceptionMapper implements ExceptionMapper<ReaderException> {
    private static Logger logger = LoggerFactory.getLogger(ReaderExceptionMapper.class);
    
    @Context
    HttpServletRequest request;
    
    @Override
    public Response toResponse(ReaderException ex) {
        String method = null;
        String uri = null;
        if (request != null) {
            uri = request.getRequestURI();
            method = request.getMethod();
        }
        
        ActionResponse response = new ActionResponse();
        response.setResultCode(InformationCodes.PARSING_ERROR);

        Throwable cause = ex.getCause();
        if (cause instanceof JsonParseException) {
            String message = cause.getMessage();
            if (message != null) {
                response.setResultCodeMessage(extractSourceObject(message));

                if (logger.isInfoEnabled()) {
                    logger.info("JsonParseException in " + method + ':' + uri + " : " + message);
                }
            }
        } else if (cause instanceof IllegalArgumentException) {
            if (logger.isInfoEnabled()) {
                logger.info(cause.getClass().getName() + " in " + method + ':' + uri + " : " + cause.toString());
            }
            response.setResultCode(InformationCodes.WRONG_PARAM_VALUE);
            response.setResultCodeMessage(cause.getMessage());
        } else if (cause instanceof IOException) {
            if (logger.isInfoEnabled()) {
                logger.info("IOException in " + method + ':' + uri);
            }
        } else {
            logger.warn("Reader exception in " + method + ":" + uri + " : " + ex.toString());
        }

        if (request != null) {
            request.setAttribute(ApiResponse.API_RESPONSE, response);
        }
        
        return makeMapperResponse(uri, response);
    }
    
    private String extractSourceObject(String message) {
        String pat1 = "[Source: ";
        String pat2 = "line:";
        int start = 0;
        StringBuilder buf = new StringBuilder(message.length());
        
        for (;;) {
            int ind1 = message.indexOf(pat1, start);
            if (ind1 < 0) break;
        
            int ind2 = message.indexOf(pat2, ind1);
            if (ind2 < 0) break;

            buf.append(message.substring(start, ind1 + pat1.length()));
            start = ind2;
        }
        
        buf.append(message.substring(start));
        
        return buf.toString();
    }
}
