package com.jbb.server.common.exception;

public class AliPayException extends BaseLogicalException {

    /**
     *
     */
    private static final long serialVersionUID = 1182702269118237190L;

    public AliPayException() {
        super(ALIPAY_ERROR, "jbb.error.exception.alipay.error", "zh");
    }

    public AliPayException(String message) {
        super(ALIPAY_ERROR, message, "zh");
    }

    public AliPayException(String messageId, String error) {
        super(ALIPAY_ERROR, messageId, "zh", error);
    }

}
