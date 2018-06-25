package com.jbb.server.common.lang;

import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.common.util.StringUtil;
import com.jbb.server.common.PropertyManager;

/**
 * LanguageHelper
 * @author VincentTang
 * @date 2017年12月20日
 */
public class LanguageHelper {
	private static Logger logger = LoggerFactory.getLogger(LanguageHelper.class);

	private static final String LANG_NAME_ZH = "zh";
	public static final String DEFAULT_LANG = PropertyManager.getProperty("jbb.lang.default", LANG_NAME_ZH);
	private static final String LANG_RESOURCE_PREFIX = "lang.errorMessages_";

	public static String getLocalizedMessage(String messageId, String language) {
		if (language == null)
			language = DEFAULT_LANG;

		String message;
		try {
			ResourceBundle resourceBundle = ResourceBundle.getBundle(LANG_RESOURCE_PREFIX + language);
			message = resourceBundle.getString(messageId);
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.info("Exception in getting resource for language=" + language + ", messageId=" + messageId
						+ " : " + e.toString());
			}

			try {
				ResourceBundle resourceBundle = ResourceBundle.getBundle(LANG_RESOURCE_PREFIX + LANG_NAME_ZH);
				message = resourceBundle.getString(messageId);
			} catch (Exception ee) {
				logger.error("Exception in getting resource for language=" + language + ", messageId=" + messageId
						+ " : " + ee.toString());
				message = messageId;
			}
		}

		return message;
	}

	public static String getLocalizedMessage(String messageId, String language, String parameter0) {
		String localizedMessage = getLocalizedMessage(messageId, language);
		if (localizedMessage == null)
			return null;

		if (parameter0 != null) {
			localizedMessage = StringUtil.replace(localizedMessage, "[0]", parameter0);
		}

		return localizedMessage;
	}

	public static String getLocalizedMessage(String messageId, String language, String... parameters) {
		String localizedMessage = getLocalizedMessage(messageId, language);
		if (localizedMessage == null)
			return null;

		if ((parameters != null) && (parameters.length > 0)) {
			localizedMessage = StringUtil.replaceIndexedPattern(localizedMessage, parameters);
		}

		return localizedMessage;
	}

	public static <T> T getLocalizedValue(Map<String, T> multilangValue, String language) {
		if ((multilangValue == null) || multilangValue.isEmpty()) {
			return null;
		}

		T localizedValue;

		if (language != null) {
			localizedValue = multilangValue.get(language);
			if (localizedValue != null)
				return localizedValue;
		}

		localizedValue = multilangValue.get(DEFAULT_LANG);
		if (localizedValue != null)
			return localizedValue;

		localizedValue = multilangValue.get(LANG_NAME_ZH);
		if (localizedValue != null)
			return localizedValue;

		return multilangValue.values().iterator().next();
	}
}
