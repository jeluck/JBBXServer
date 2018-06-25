package com.jbb.mgt.server.rs.action;

import java.util.Calendar;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jbb.mgt.core.domain.Account;
import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.core.service.AccountService;
import com.jbb.mgt.core.service.UserService;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.common.Constants;
import com.jbb.server.common.exception.AccessDeniedException;
import com.jbb.server.common.exception.WrongUserKeyException;
import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.HttpUtil;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.shared.rs.InformationCodes;

public abstract class BasicComponent implements InformationCodes {
	private static Logger logger = LoggerFactory.getLogger(BasicComponent.class);
	
	
	private static final String JBB_PLATFORM_API_KEY = "9Ol7ipefXdcSD_tWi6raaIdrmHJDas";

	private ActionResponse actionResponse = makeActionResponse();
	/** Account. !!! Do not modify this user object. It is in cache memory. */
	protected Account account;

	protected User user;

	private HttpServletRequest httpRequest;
	private TimeZone requestTimezone;
	private Calendar requestCalendar;

	@Autowired
	protected AccountService coreAccountService;

	@Autowired
	private UserService userService;
	
	 public void validateJbbPlatformApiKey(String apiKey) throws WrongUserKeyException {
	       if(!JBB_PLATFORM_API_KEY.equals(apiKey)){
	           throw new WrongUserKeyException();
	       }
	    }

	public void setContextInstances(HttpServletRequest request) {
		if (request != null) {
			this.httpRequest = request;
			this.setTimezone(request.getParameter("timeZone"));
		}
	}

	public void validateUserKey(String userKey) throws WrongUserKeyException {
		authenticate(userKey);
	}

	private void authenticate(String userKey) throws WrongUserKeyException {
		account = coreAccountService.login(userKey);
		if (account == null) {
			logger.error("Not found user, API_KEY = " + userKey);
			throw new WrongUserKeyException();
		}

		logger.debug("Authenticated account: {}", account);
	}

	// validate 进件用户的userKey
	public void validateEntryUserKey(String userKey) throws WrongUserKeyException {
		authenticateEntryUserByUserKey(userKey);
	}

	private void authenticateEntryUserByUserKey(String userKey) throws WrongUserKeyException {
		user = userService.login(userKey);
		if (user == null) {
			logger.error("Not found user, USER_KEY = " + userKey);
			throw new WrongUserKeyException();
		}

		logger.debug("Authenticated entry user: {}", user);
	}
	// End validate 进件用户的userKey

	/**
	 * 
	 * @param permissions
	 *            访问此接口需要的权限
	 */
	public void validateUserAccess(int[] permissions) {
		boolean flag = false;

		// 系统管理员 可访问任何接口
		if (CommonUtil.inArray(Constants.MGT_P_SYSADMIN, this.account.getPermissions())) {
			return;
		}

		// 其他按接口访问权限匹配用户权限 ，如果存在权限 ，再可访问，否则抛出异常
		for (int p : permissions) {
			if (CommonUtil.inArray(p, this.account.getPermissions())) {
				flag = true;
				break;
			}
		}

		if (!flag) {
			throw new AccessDeniedException("jbb.error.validateAdminAccess.accessDenied", "zh");
		}

	}

	public boolean isSysAdmin() {
		return CommonUtil.inArray(Constants.MGT_P_SYSADMIN, this.account.getPermissions());
	}

	public boolean isOrgAdmin() {
		return CommonUtil.inArray(Constants.MGT_P_ORGADMIN, this.account.getPermissions());
	}

	protected String getRemoteAddress() {
		return httpRequest != null ? HttpUtil.getClientIpAddress(httpRequest) : null;
	}

	protected ActionResponse makeActionResponse() {
		return null;
	}

	public void escalateErrorCode(int forceErrorCode) {
		actionResponse.escalateErrorCode(forceErrorCode);
	}

	public void escalateErrorCode() {
		actionResponse.escalateErrorCode(FAILURE);
	}

	public void setResultCode(int resultCode, String resultCodeMessage) {
		actionResponse.setResultCodeAndMessage(resultCode, resultCodeMessage);
	}

	public final ActionResponse getActionResponse() {
		return actionResponse;
	}

	public TimeZone getTimezone() {
		if (requestTimezone != null)
			return requestTimezone;
		return TimeZone.getDefault();
	}

	public void setTimezone(String timezone) {
		if (timezone != null) {
			this.requestTimezone = TimeZone.getTimeZone(timezone);
			this.requestCalendar = DateUtil.getCurrentCalendar(requestTimezone);
		}
	}

}
