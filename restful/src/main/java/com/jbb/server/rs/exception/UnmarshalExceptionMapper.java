package com.jbb.server.rs.exception;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.plugins.providers.jaxb.JAXBUnmarshalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXParseException;

import com.jbb.server.rs.pojo.ActionResponse;
import com.jbb.server.rs.pojo.ApiResponse;
import com.jbb.server.shared.rs.InformationCodes;

@Provider
public class UnmarshalExceptionMapper extends BaseExceptionMapper implements ExceptionMapper<JAXBUnmarshalException> {
    private static Logger logger = LoggerFactory.getLogger(UnmarshalExceptionMapper.class);
    
    @Context
    private HttpServletRequest request;
    
    @Override
	public Response toResponse(JAXBUnmarshalException ex) {
    	String method = null;
    	String uri = null;
    	if (request != null) {
    		uri = request.getRequestURI();
    		method = request.getMethod();
    	}

    	ActionResponse response = new ActionResponse();
    	response.setResultCode(InformationCodes.PARSING_ERROR);

    	Throwable e1 = ex.getCause();
    	if (e1 != null) {
        	Throwable e2 = e1.getCause();
        	if (e2 instanceof SAXParseException) {
        		String message = e2.getMessage();
	    		response.setResultCodeMessage(message);

	    		if (logger.isInfoEnabled()) {
	                StringBuilder errorMsg = new StringBuilder(250);
	                errorMsg.append("SAXParseException in ").append(method).append(':').append(uri).append(" : ").append(message);
	    			logger.info(errorMsg.toString());
	    		}
        	} else {
            	logger.warn("Unmarshal exception in " + method + ":" + uri + " : " + ex.toString());
        	}
    	} else {
        	logger.warn("Unmarshal exception in " + method + ":" + uri + " : " + ex.toString());
    	}

        if (request != null) {
            request.setAttribute(ApiResponse.API_RESPONSE, response);
        }
    	
    	return makeMapperResponse(uri, response);
	}
}
