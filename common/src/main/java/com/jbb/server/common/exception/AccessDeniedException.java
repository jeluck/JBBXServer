package com.jbb.server.common.exception;

public class AccessDeniedException extends BaseLogicalException {
    private static final long serialVersionUID = 2996968580803428446L;

    public AccessDeniedException(String apiErrorMessage) {
    	super(ACCESS_DENIED, apiErrorMessage, "zh");
    }
    
    public AccessDeniedException(String apiErrorMessage, String language) {
        super(ACCESS_DENIED, apiErrorMessage, language);
    }

    public AccessDeniedException(String apiErrorMessage, String language, String param0) {
        super(ACCESS_DENIED, apiErrorMessage, language, param0);
    }
}
