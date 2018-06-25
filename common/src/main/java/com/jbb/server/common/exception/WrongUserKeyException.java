package com.jbb.server.common.exception;

public class WrongUserKeyException extends BaseLogicalException {
	private static final long serialVersionUID = -8914796182070096521L;

	public WrongUserKeyException() {
		super(WRONG_API_KEY, "jbb.error.exception.wrongUserKey", null);
	}
	
	public WrongUserKeyException(String messageId) {
		super(WRONG_API_KEY, messageId, null);
	}
}
