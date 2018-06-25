package com.jbb.server.rs.exception;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.plugins.providers.jaxb.JAXBMarshalException;
import org.jboss.resteasy.spi.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class WriterExceptionMapper extends BaseExceptionMapper implements ExceptionMapper<WriterException> {
    private static Logger logger = LoggerFactory.getLogger(WriterExceptionMapper.class);
    
    @Context
    HttpServletRequest request;
    @Context
    HttpServletResponse response;
    
    @Override
	public Response toResponse(WriterException ex) {
        StringBuilder sb = new StringBuilder();
        sb.append("Writer exception ");
    	
    	if (request != null) {
    		sb.append("in ").append(request.getMethod())
    		    .append(':').append(request.getRequestURI());
    	}
    	
        if (response != null) {
            sb.append(", contentLength=").append(response.getHeader("Content-Length"))
                .append(", bufferSize=").append(response.getBufferSize());
        }
        
        sb.append(" : ").append(ex.toString());

        if (request != null) {
            sb.append("\n  Request headers:")
                .append("\n  UserAgent=").append(request.getHeader("User-Agent"))
                .append("\n  Range=").append(request.getHeader("Range"))
                .append("\n  RemoteHost=").append(request.getRemoteHost())
                .append("\n  RemoteAddr=").append(request.getRemoteAddr())
                .append("\n  Query=").append(request.getQueryString());
    	}
    	
        if (ex instanceof JAXBMarshalException) {
            logger.error(sb.toString());
        } else if (ex.getCause() instanceof IOException) {
            if (logger.isInfoEnabled()) {
                logger.info("IOException in " + sb.toString());
            }
        } else {
            logger.warn(sb.toString());
        }
    	
    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}
}
