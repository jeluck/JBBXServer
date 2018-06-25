package com.jbb.mgt.server.rs.exception;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbb.server.common.exception.BaseLogicalException;
import com.jbb.server.common.exception.ExecutionException;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.mgt.server.rs.pojo.ApiResponse;
import com.jbb.server.shared.rs.InformationCodes;

public abstract class BaseExceptionMapper {
	private static Logger logger = LoggerFactory.getLogger(BaseExceptionMapper.class);

	private static final int[] NO_WARN_CODES = { InformationCodes.WRONG_API_KEY, InformationCodes.ACCESS_DENIED };

	private static ObjectMapper objectMapper = new ObjectMapper();
	private static ConcurrentHashMap<Class<? extends ActionResponse>, JAXBContext> jaxbOutContexts = new ConcurrentHashMap<>(
			32);
	private static CacheControl CACHE_CONTROL = new CacheControl();

	static {
		CACHE_CONTROL.setNoCache(true);
		CACHE_CONTROL.setNoStore(true);
	}

	public static boolean noWarn(int errorCode) {
		for (int code : NO_WARN_CODES) {
			if (errorCode == code)
				return true;
		}
		return false;
	}

	private byte[] generateJson(ActionResponse response) throws JsonProcessingException {
		return objectMapper.writeValueAsBytes(response);
	}

	private byte[] generateXml(ActionResponse response) throws JAXBException {
		Class<? extends ActionResponse> clazz = response.getClass();
		JAXBContext jc = jaxbOutContexts.get(clazz);
		if (jc == null) {
			jc = JAXBContext.newInstance(clazz);
			jaxbOutContexts.put(clazz, jc);
		}
		Marshaller jaxbMarshaller = jc.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		jaxbMarshaller.marshal(response, bos);

		return bos.toByteArray();
	}

	Response makeMapperResponse(String uri, ActionResponse response) {
		try {
			MediaType type;
			byte[] res;

			if ((uri != null) && (uri.toLowerCase().indexOf("/xml/") > 0)) {
				type = MediaType.APPLICATION_XML_TYPE;
				res = generateXml(response);
			} else {
				type = MediaType.APPLICATION_JSON_TYPE;
				res = generateJson(response);
			}

			return Response.ok(res, type).cacheControl(CACHE_CONTROL).build();
		} catch (Exception e) {
			logger.error("Exception in generating response", e);
		}

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

	Response makeServerErrorResponse(Exception ex, HttpServletRequest request) {
		String method = null;
		String uri = null;
		String userAgent = null;
		if (request != null) {
			uri = request.getRequestURI();
			method = request.getMethod();
			userAgent = request.getHeader(HttpHeaders.USER_AGENT);
		}

		ActionResponse response = new ActionResponse();
		String msgHead = "Error";
		int errorCode = 0;
		String errorMessage = null;
		short logLevel = 0;
		final short INFO = 3;
		final short WARN = 4;
		final short ERROR = 5;

		Throwable cause = ex.getCause();
		if (cause != null) {
			for (int i = 0; i < 10; i++) {
				if ((cause instanceof BaseLogicalException) || (cause instanceof ExecutionException))
					break;

				Throwable c = cause.getCause();
				if (c == null)
					break;

				cause = c;
			}
		}

		if (cause instanceof BaseLogicalException) {
			BaseLogicalException le = (BaseLogicalException) cause;
			errorCode = le.getApiErrorCode();
			errorMessage = le.getApiErrorMessage();

			response.setResultCodeAndMessage(errorCode, errorMessage);
			logLevel = noWarn(errorCode) ? INFO : WARN;

		} else if (cause instanceof IllegalArgumentException) {
			msgHead = cause.getClass().getName();
			errorMessage = cause.toString();
			logLevel = INFO;
			response.escalateErrorCode(InformationCodes.WRONG_PARAM_VALUE);
		} else if ((cause instanceof IOException) || (cause instanceof IOExceptionWrapper)) {
			msgHead = cause.toString();
			logLevel = INFO;
			response.escalateErrorCode(InformationCodes.FAILURE);
		} else if (cause instanceof ExecutionException) {
			msgHead = cause.toString();
			logLevel = ERROR;
			response.escalateErrorCode(InformationCodes.FAILURE);
		} else {
			logger.error("Exception in " + method + ":" + uri, ex);
			response.escalateErrorCode(InformationCodes.FAILURE);
		}

		if ((logLevel > 0) && ((INFO != logLevel) || logger.isInfoEnabled())) {
			StringBuilder errorMsgBuf = new StringBuilder(250);
			errorMsgBuf.append(msgHead).append(" in ").append(method).append(':').append(uri);
			if (errorCode > 0)
				errorMsgBuf.append(", resultCode=").append(errorCode);
			if (errorMessage != null)
				errorMsgBuf.append(" : ").append(errorMessage);
			errorMsgBuf.append("\nUser-Agent: ").append(userAgent);

			if (INFO == logLevel)
				logger.info(errorMsgBuf.toString());
			else if (WARN == logLevel)
				logger.warn(errorMsgBuf.toString());
			else
				logger.error(errorMsgBuf.toString());
		}

		if (request != null) {
			request.setAttribute(ApiResponse.API_RESPONSE, response);
		}

		return makeMapperResponse(uri, response);
	}
}
