package com.jbb.mgt.server.rs.exception;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.ApplicationException;

@Provider
public class ApplicationExceptionMapper extends BaseExceptionMapper implements ExceptionMapper<ApplicationException> {
    @Context
    private HttpServletRequest request;
    
    @Override
	public Response toResponse(ApplicationException ex) {
        return makeServerErrorResponse(ex, request);
    }
}
