 package com.jbb.server.common.exception;

 public class ChuangLanException extends BaseLogicalException {

	/**
	 *serialVersionUID
	 */
	private static final long serialVersionUID = 8056118973922507360L;
	
	public ChuangLanException() {
		super(CHUANGLAN_REQUEST_ERROR, "jbb.error.exception.chuanlan.error", "zh");
	}
	
	public ChuangLanException(String message) {
        super(CHUANGLAN_REQUEST_ERROR, message, "zh");
    }
	
	public ChuangLanException(String messageId, String error) {
        super(CHUANGLAN_REQUEST_ERROR, messageId, "zh", error);
    }

}
