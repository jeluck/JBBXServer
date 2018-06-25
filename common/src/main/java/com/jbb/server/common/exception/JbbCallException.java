package com.jbb.server.common.exception;

public class JbbCallException extends BaseLogicalException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8056118973922507360L;

	public JbbCallException() {
		super(JBB_ERROR, "jbb.error.exception.aliyun.error", "zh");
	}

	public JbbCallException(String message) {
		super(JBB_ERROR, message, "zh");
	}

	public JbbCallException(String messageId, String error) {
		super(JBB_ERROR, messageId, "zh", error);
	}

}
