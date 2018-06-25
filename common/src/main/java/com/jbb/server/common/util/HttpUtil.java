package com.jbb.server.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.common.Constants;
import com.jbb.server.common.exception.ExecutionException;

/**
 * Wrapper for common HTTP functions such as HTTP GET, HTTP POST and also
 * posting XML content.
 * @author VincentTang
 * @date 2017年12月20日
 */
public class HttpUtil {
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	public static final String HTTP_GET = "GET";
	public static final String HTTP_POST = "POST";
	public static final String HTTP_PUT = "PUT";
	public static final String HTTP_DELETE = "DELETE";
	// public static final String HTTP_OPTIONS = "OPTIONS";
	// public static final String HTTP_HEAD = "HEAD";

	public static final String CONTENT_TYPE_JSON = "application/json; charset=\"utf-8\"";
	
	public static final String CONTENT_TYPE_FORM_DATA_BOUNDARY="--jbbmgtrequest";
	public static final String CONTENT_TYPE_FORM_DATA_WITH_BOUNDARY = "multipart/form-data; boundary=--"+ CONTENT_TYPE_FORM_DATA_BOUNDARY;
	
	public static final String CONTENT_TYPE_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded; charset=\"utf-8\"";
	// public static final String CONTENT_TYPE_XML = "application/xml;
	// charset=\"utf-8\"";
	public static final String CONTENT_TYPE_TEXT_XML = "text/xml; charset=\"utf-8\"";

	public static final int STATUS_OK = 200;

	private static final String PROXY_HEADER_CLIENT_IP = "X-Forwarded-For";
	
	public static final String HEADER_CONTENT_TYPE = "Content-Type";

    public static final String CONTENT_TYPE_FORM_DATA = "multipart/form-data";

	public static byte[] httpGet(String urlString) throws Exception {
		return httpGet(urlString, null);
	}

	/**
	 * Issues an HTTP GET request against the specified URL and returns the
	 * results as a string
	 * 
	 * @param urlString
	 *            the URL plus all get parameters
	 * @param requestProperties
	 *            a map of HTTP request properties (headers)
	 * @return the response from the HTTP GET operation, as a String
	 */
	private static InputStream httpGetStream(String urlString, String[][] requestProperties) throws Exception {
		logger.debug(">httpGetStream() url='{}'", urlString);

		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(HTTP_GET);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setAllowUserInteraction(false);

		if (requestProperties != null) {
			for (String[] prop : requestProperties) {
				connection.setRequestProperty(prop[0], prop[1]);
			}
		}

		connection.connect();
		int responseCode = connection.getResponseCode();
		if (logger.isDebugEnabled())
			logger.debug("responseCode=" + responseCode);

		InputStream in;
		if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
			in = connection.getInputStream();
		} else {
			in = connection.getErrorStream();

			if (logger.isDebugEnabled()) {
				String msg = connection.getResponseMessage();
				logger.debug("Response code: " + responseCode + " " + msg);
			}
		}

		if (in == null) {
			throw new ExecutionException("Cannot read response from [" + urlString + "], responseCode=" + responseCode);
		}

		logger.debug("<httpGetStream()");
		return in;
	}

	/**
	 * Issues an HTTP GET request against the specified URL and returns the
	 * results as a string
	 * 
	 * @param urlString
	 *            the URL plus all get parameters
	 * @param requestProperties
	 *            a map of HTTP request properties (headers)
	 * @return the response from the HTTP GET operation, as a String
	 */
	public static byte[] httpGet(String urlString, String[][] requestProperties) throws Exception {
		logger.debug(">httpGet()");
		byte[] res;

		try (InputStream in = httpGetStream(urlString, requestProperties)) {
			res = IOUtil.readStreamClean(in);
		}

		logger.debug("<httpGet()");
		return res;
	}

	/**
	 * Posts the XML data to the specified URL and returns the results as a
	 * string
	 * 
	 * @param urlString
	 *            the URL to issue the HTTP POST request against
	 * @param xmlText
	 *            the XML data to be posted
	 * @return the response from the HTTP POST operation, as a String
	 */
	public static String xmlPost(String urlString, String xmlText) throws Exception {
		return new String(xmlPost(urlString, xmlText, null));
	}

	/**
	 * Posts the XML data to the specified URL and returns the results as a byte
	 * array
	 * 
	 * @param urlString
	 *            the URL to issue the HTTP POST request against
	 * @param xmlText
	 *            the XML data to be posted
	 * @param requestProperties
	 *            a map of HTTP request properties (headers)
	 * @return the response from the HTTP POST operation
	 */
	public static byte[] xmlPost(String urlString, String xmlText, String[][] requestProperties) throws IOException {
		logger.debug(">xmlPost() urlString={}", urlString);

		HttpResponse res = sendDataViaHTTP(HTTP_POST, urlString, "application/xml; charset=\"utf-8\"", xmlText,
				requestProperties);

		logger.debug("<xmlPost()");
		return res.responseBody;
	}

	public static class HttpResponse {
		private int responseCode;
		private byte[] responseBody;
		private boolean sent;
		private String errorMessage;

		public HttpResponse(int responseCode, byte[] responseBody, String errorMessage) {
			this.responseCode = responseCode;
			this.responseBody = responseBody;
			this.sent = responseCode < HttpURLConnection.HTTP_INTERNAL_ERROR;
			this.errorMessage = errorMessage;
		}

		public HttpResponse(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		public boolean isOkResponseCode() {
			return HttpURLConnection.HTTP_OK == responseCode;
		}

		/**
		 * Combined response check
		 * 
		 * @return true, if the request has been sent, the response code is OK,
		 *         the response body exists
		 */
		public boolean isResponseBody() {
			return sent && isOkResponseCode() && (responseBody != null) && (responseBody.length > 0);
		}

		public int getResponseCode() {
			return responseCode;
		}

		public byte[] getResponseBody() {
			return responseBody;
		}

		public boolean isSent() {
			return sent;
		}

		public void setSent(boolean sent) {
			this.sent = sent;
		}

		public String getErrorMessage() {
			return errorMessage;
		}
	}

	/**
	 * Sends data to the specified URL and returns the results
	 * 
	 * @param requestMethod
	 *            the method to use, either GET or POST or PUT
	 * @param urlString
	 *            the URL to issue the HTTP request against
	 * @param contentType
	 *            the content type to use, If null, no content type will be set
	 * @param data
	 *            the data to be sent (String or byte[])
	 * @param requestProperties
	 *            an array of HTTP request properties (headers) in format of
	 *            {name,value}
	 * @return the response from the HTTP operation
	 */
	public static HttpResponse sendDataViaHTTP(String requestMethod, String urlString, String contentType, Object data,
			String[][] requestProperties) throws IOException {
		logger.debug(">sendDataViaHTTP() {} {}", requestMethod, urlString);

		URL url = new URL(urlString);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

		urlConn.setRequestMethod(requestMethod);
		urlConn.setDoOutput(true);
		urlConn.setDoInput(true);
		urlConn.setUseCaches(false);

		if (requestProperties != null) {
			for (String[] propPair : requestProperties) {
				urlConn.setRequestProperty(propPair[0], propPair[1]);
			}
		}

		if (data != null) {
			if (contentType != null) {
				urlConn.setRequestProperty("Content-Type", contentType);
			}

			if (data instanceof String) {
				String text = (String) data;
				urlConn.setRequestProperty("Content-Length", String.valueOf(text.length())); // get
																								// length
																								// in
																								// UTF-8

				try (OutputStreamWriter out = new OutputStreamWriter(urlConn.getOutputStream(), Constants.UTF8)) {
					out.write(text);
					out.flush();
				}
			} else if (data instanceof byte[]) {
				byte[] dataArr = (byte[]) data;
				urlConn.setRequestProperty("Content-Length", String.valueOf(dataArr.length));

				try (OutputStream out = urlConn.getOutputStream()) {
					out.write(dataArr);
					out.flush();
				}
			} else if (data instanceof File) {
				File file = (File) data;

				urlConn.setRequestProperty("Content-Length", String.valueOf(file.length()));

				try (OutputStream out = urlConn.getOutputStream(); FileInputStream is = new FileInputStream(file)) {
					byte[] buffer = new byte[10240];

					for (;;) {
						int byteRead = is.read(buffer);
						if (byteRead <= 0)
							break;

						out.write(buffer, 0, byteRead);
					}

					out.flush();
				}
			} else {
				throw new ExecutionException(
						"Unsupported data type in sendDataViaHTTP: it can be String or byte[] or File");
			}
		}

		urlConn.connect();
		int responseCode = urlConn.getResponseCode();

		InputStream in = null;
		byte[] responseBody = null;
		String errorMessage = null;

		try {
			if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
				in = urlConn.getInputStream();

				if (in == null) {
					throw new ExecutionException("Cannot get response stream for urlString=[" + urlString
							+ "], responseCode=" + responseCode);
				}

				responseBody = IOUtil.readStreamClean(in);
			} else {
				in = urlConn.getErrorStream();
				if (in == null)
					in = urlConn.getInputStream();

				if (in != null) {
					responseBody = IOUtil.readStreamClean(in);
				}

				errorMessage = urlConn.getResponseMessage();

				if (logger.isDebugEnabled()) {
					logger.debug("Response code: " + responseCode + ", " + errorMessage);
				}
			}
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (Exception e) {
				logger.warn("Exception in closing input stream " + e.toString());
			}
		}

		logger.debug("<sendDataViaHTTP()");
		return new HttpResponse(responseCode, responseBody, errorMessage);
	}

	/**
	 * Sends data to the specified URL and returns the results. The operation is
	 * repeated several times untill success
	 * 
	 * @param requestMethod
	 *            the method to use, either GET or POST or PUT
	 * @param urlString
	 *            the URL to issue the HTTP request against
	 * @param contentType
	 *            the content type to use, If null, no content type will be set
	 * @param data
	 *            the data to be sent (String or byte[])
	 * @param requestProperties
	 *            an array of HTTP request properties (headers) in format of
	 *            {name,value}
	 * @param methodName
	 *            name for error logging
	 * @return the response from the HTTP operation
	 */
	public static HttpResponse sendDataViaHTTPRepeat(String requestMethod, String urlString, String contentType,
			Object data, String[][] requestProperties, String methodName) {
		boolean sent = false;
		HttpResponse resp = null;
		String errorMessage = null;

		for (int i = 3; !sent && (i >= 0); i--) {
			try {
				resp = HttpUtil.sendDataViaHTTP(requestMethod, urlString, contentType, data, requestProperties);
				int respCode = resp.getResponseCode();

				if (respCode < HttpURLConnection.HTTP_INTERNAL_ERROR) {
					sent = true;
				} else {
					logger.warn(errorMessage = "Internal error in " + methodName + ", error code: " + respCode);
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e1) {
						// ignore
					}
				}
			} catch (ExecutionException e) {
				logger.warn("Exception in " + methodName + " : " + e.toString());
				throw e;
			} catch (Exception e) {
				logger.warn(errorMessage = "Exception in " + methodName + " : " + e.toString());
				if (i > 0) {
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e1) {
						// ignore
					}
				}
			}
		}

		return resp != null ? resp : new HttpResponse(errorMessage);
	}

	/**
	 * Posts the JSON data to the specified URL and returns the results
	 * 
	 * @param urlString
	 *            the URL to issue the HTTP POST request against
	 * @param jsonText
	 *            the JSON data to be posted
	 * @param requestProperties
	 *            a map of HTTP request properties (headers)
	 * @param methodName
	 *            name for error logging
	 * @return the response from the HTTP POST operation
	 */
	public static HttpResponse jsonPost(String urlString, String jsonText, String[][] requestProperties,
			String methodName) {
		return sendDataViaHTTPRepeat(HTTP_POST, urlString, CONTENT_TYPE_JSON, jsonText, requestProperties, methodName);
	}

	/**
	 * Encode URL parameter for HTTP request (uses java.net.URLEncoder)
	 * 
	 * @param paramName
	 *            parameter name
	 * @param paramValue
	 *            parameter value
	 * @return encoded string in UTF-8 encoding
	 */
	public static String encodeURLParam(String paramName, String paramValue) {
		try {
			return paramName.concat("=").concat(URLEncoder.encode(StringUtil.notNull(paramValue), Constants.UTF8));
		} catch (UnsupportedEncodingException e) {
			throw new ExecutionException(e);
		}
	}

	/**
	 * Encode URL parameter value for HTTP request (uses java.net.URLEncoder)
	 * 
	 * @param paramValue
	 *            parameter value
	 * @return encoded value in UTF-8 encoding
	 */
	public static String encodeURLParam(String paramValue) {
		try {
			return URLEncoder.encode(paramValue, Constants.UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new ExecutionException(e);
		}
	}

	/**
	 * Decode URL parameter value (uses java.net.URLDecoder)
	 * 
	 * @param paramValue
	 *            parameter value encoded in UTF-8
	 * @return decoded value
	 */
	public static String decodeURLParam(String paramValue) {
		try {
			return URLDecoder.decode(paramValue, Constants.UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new ExecutionException(e);
		}
	}

	/**
	 * Extract HTTP header values to map
	 * 
	 * @return key/value map of the header
	 */
	public static Map<String, String> extractHeaderValues(Enumeration<String> headers) {
		HashMap<String, String> map = new HashMap<>();

		if (headers != null) {
			while (headers.hasMoreElements()) {
				String h = headers.nextElement();
				if (h != null)
					extractHeader(h, map);
			}
		}

		return map;
	}

	public static String extractHeaderValue(Enumeration<String> headers, String name) {
		if (headers == null)
			return null;

		while (headers.hasMoreElements()) {
			String h = headers.nextElement();
			if (h == null)
				continue;

			int ind = h.indexOf('=');
			if (ind <= 0)
				continue;

			String key = h.substring(0, ind).trim();
			if (!key.equals(name))
				continue;

			String value = h.substring(ind + 1).trim();
			int valueLen = value.length();
			if ((valueLen > 0) && ('"' == value.charAt(0)) && ('"' == value.charAt(valueLen - 1))) {
				value = value.substring(1, valueLen - 1);
			}

			return value;
		}

		return null;
	}

	/**
	 * Extract HTTP header values to map
	 * 
	 * @return key/value map of the header
	 */
	public static Map<String, String> extractHeaderValues(List<String> headers) {
		HashMap<String, String> map = new HashMap<>();

		if (headers != null) {
			for (String hStr : headers) {
				extractHeader(hStr, map);
			}
		}

		return map;
	}

	private static void extractHeader(String hStr, Map<String, String> map) {
		int ind = hStr.indexOf('=');
		if (ind <= 0)
			return;

		String key = hStr.substring(0, ind).trim();
		String value = hStr.substring(ind + 1).trim();
		int valueLen = value.length();
		if ((valueLen > 0) && ('"' == value.charAt(0)) && ('"' == value.charAt(valueLen - 1))) {
			value = value.substring(1, valueLen - 1);
		}

		map.put(key, value);
	}

	public static String getClientIpAddress(HttpServletRequest request) {
		String userIpAddr = null;
		// get user IP address from proxy headers
		Enumeration<String> clientIpHeaders = request.getHeaders(PROXY_HEADER_CLIENT_IP);
		if (clientIpHeaders != null) {
			while (clientIpHeaders.hasMoreElements()) {
				String h = clientIpHeaders.nextElement();
				if (h != null)
					userIpAddr = h;
			}
		}

		if (userIpAddr == null) {
			userIpAddr = request.getRemoteAddr();
		}

		return userIpAddr;
	}
}
