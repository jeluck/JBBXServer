package com.jbb.server.shared.rs;

import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.ExecutionException;
import com.jbb.server.common.util.StringUtil;

public class RsHelper {
	/** Return RESTful API host */
	public static String getRsApiHost() {
		String host = PropertyManager.getProperty("jbb.host.api");
		if (StringUtil.isEmpty(host)) {
			throw new ExecutionException("App API host is not set");
		}
		return host;
	}

	/** Return end user web UI host */
	public static String getUserUiHost() {
		return PropertyManager.getProperty("jbb.host.web.ui");
	}

}
