package com.jbb.server.rs.exception;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServerErrorExceptionMapper extends BaseExceptionMapper implements ExceptionMapper<ServerErrorException> {
    @Context
    private HttpServletRequest request;
    
    @Override
	public Response toResponse(ServerErrorException ex) {
        return makeServerErrorResponse(ex, request);
    }
}
