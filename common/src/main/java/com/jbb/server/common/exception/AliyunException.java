 package com.jbb.server.common.exception;

 public class AliyunException extends BaseLogicalException {

	/**
	 *serialVersionUID
	 */
	private static final long serialVersionUID = 8056118973922507360L;
	
	public AliyunException() {
		super(ALIYUN_REQUEST_ERROR, "jbb.error.exception.aliyun.error", "zh");
	}
	
	public AliyunException(String message) {
        super(ALIYUN_REQUEST_ERROR, message, "zh");
    }
	
	public AliyunException(String messageId, String error) {
        super(ALIYUN_REQUEST_ERROR, messageId, "zh", error);
    }

}
