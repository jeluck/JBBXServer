package com.jbb.mgt.server.rs.services;

import java.net.URI;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jbb.server.common.exception.AccessDeniedException;
import com.jbb.server.common.exception.ObjectNotFoundException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.exception.WrongUserKeyException;
import com.jbb.server.common.util.StringUtil;
import com.jbb.mgt.server.rs.action.BasicComponent;
import com.jbb.server.common.Constants;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.shared.rs.RsHelper;

public abstract class BasicRestfulServices {
	private static Logger logger = LoggerFactory.getLogger(BasicRestfulServices.class);

	protected static final String API_KEY = Constants.API_KEY;
	static final String API_PASSWORD = "PASSWORD";
	static final String RANGE_HEADER = "Range";

	private ApplicationContext applicationContext;
	private String[] contextFiles;
	@Context
	protected HttpServletRequest request;

	@SuppressWarnings("unchecked")
	public <T> T getBean(String beanName) {
		T bean = (T) applicationContext.getBean(beanName);
		if (bean instanceof BasicComponent) {
			BasicComponent component = (BasicComponent) bean;
			component.setContextInstances(request);
		}
		return bean;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public String[] getContextFiles() {
		return contextFiles;
	}

	public void setContextFiles(String[] contextFiles) {
		this.contextFiles = contextFiles;
		try {
			applicationContext = new ClassPathXmlApplicationContext(contextFiles);
		} catch (Exception ex) {
			applicationContext = null;
		}
	}

	Response redirectToErrorPage() {
		String uiHost = RsHelper.getUserUiHost();
		String errorPage = PropertyManager.getProperty("jbb.redirect.error.page");

		if (!StringUtil.isEmpty(uiHost) && !StringUtil.isEmpty(errorPage)) {
			try {
				return Response.seeOther(new URI(uiHost + errorPage)).build();
			} catch (Exception e) {
				logger.error("Exception in error page URI creation: " + e.toString());
			}
		}

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

	Response buildResponse(Supplier<Response.Status> operation, Supplier<String> errorMessage) {
		Response.Status status;
		try {
			status = operation.get();
		} catch (WrongUserKeyException e) {
			status = Response.Status.UNAUTHORIZED;
		} catch (AccessDeniedException e) {
			status = Response.Status.FORBIDDEN;
		} catch (ObjectNotFoundException e) {
			if (logger.isInfoEnabled())
				logger.info("not found: " + e.toString());
			status = Response.Status.NOT_FOUND;
		} catch (WrongParameterValueException e) {
			status = Response.Status.BAD_REQUEST;
		} catch (Exception e) {
			logger.error("Exception in " + errorMessage.get(), e);
			status = Response.Status.INTERNAL_SERVER_ERROR;
		}

		return status == null ? null : Response.status(status).build();
	}
}
