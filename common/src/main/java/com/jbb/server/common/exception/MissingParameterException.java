package com.jbb.server.common.exception;

public class MissingParameterException extends BaseLogicalException {

    private static final long serialVersionUID = -2567346903444426497L;

    public MissingParameterException(String message) {
        super(MISSED_PARAMETER, message,"zh");
    }
    
    public MissingParameterException(String messageId, String language) {
        super(MISSED_PARAMETER, messageId, language);
    }
    
    public MissingParameterException(String messageId, String language, String parameter0) {
        super(MISSED_PARAMETER, messageId, language, parameter0);
    }
}
