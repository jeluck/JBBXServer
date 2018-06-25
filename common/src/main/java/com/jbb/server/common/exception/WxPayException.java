package com.jbb.server.common.exception;

public class WxPayException extends BaseLogicalException {

    /**
     *
     */
    private static final long serialVersionUID = 1182702269118237190L;

    public WxPayException() {
        super(WX_PAY_ERROR, "jbb.error.exception.wxpay.error", "zh");
    }

    public WxPayException(String message) {
        super(WX_PAY_ERROR, message, "zh");
    }

    public WxPayException(String messageId, String error) {
        super(WX_PAY_ERROR, messageId, "zh", error);
    }

}
