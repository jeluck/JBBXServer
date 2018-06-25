
package com.jbb.server.rs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.core.service.ClubMofangService;
import com.jbb.server.core.service.SystemService;
import com.jbb.server.rs.pojo.ActionResponse;

@Service(ClubAuthorizationAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ClubAuthorizationAction extends BasicAction {
	private static Logger logger = LoggerFactory.getLogger(ClubAuthorizationAction.class);
	public static final String ACTION_NAME = "ClubAuthorizationAction";

	private Response response;
	
    @Autowired
    private ClubMofangService clubMofangService;

	@Override
	protected ActionResponse makeActionResponse() {
		return response = new Response();
	}

	public void Authorization(String realName,String identityCode,String userMobile, String userPass ) {
		logger.debug(">readLeanCloudInfo");
		clubMofangService.postAuthorizationData(realName, identityCode, userMobile, userPass);
		logger.debug("<readLeanCloudInfo");

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Response extends ActionResponse {
		private String appId;
		private String appKey;

		public String getAppId() {
			return appId;
		}

		public String getAppKey() {
			return appKey;
		}

	}
}
