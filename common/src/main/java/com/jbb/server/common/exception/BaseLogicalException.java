package com.jbb.server.common.exception;

import com.jbb.server.common.lang.LanguageHelper;
import com.jbb.server.shared.rs.InformationCodes;

public abstract class BaseLogicalException extends RuntimeException implements InformationCodes {
    private static final long serialVersionUID = 5979035236128552015L;
    
    private int apiErrorCode;
    private String apiErrorMessage;
    
    public BaseLogicalException(int apiErrorCode, String apiErrorMessage) {
        super(apiErrorMessage);
        this.apiErrorCode = apiErrorCode;
        this.apiErrorMessage = apiErrorMessage;
    }

    public BaseLogicalException(int apiErrorCode, String apiErrorMessageId, String language) {
        super(apiErrorMessageId);
        this.apiErrorCode = apiErrorCode;
        this.apiErrorMessage = LanguageHelper.getLocalizedMessage(apiErrorMessageId, language);
    }

    public BaseLogicalException(int apiErrorCode, String apiErrorMessageId, String language, String parameter0) {
        super(apiErrorMessageId);
        this.apiErrorCode = apiErrorCode;
        this.apiErrorMessage = LanguageHelper.getLocalizedMessage(apiErrorMessageId, language, parameter0);
    }

    public BaseLogicalException(int apiErrorCode, String apiErrorMessageId, String language, String parameter0, String parameter1) {
        super(apiErrorMessageId);
        this.apiErrorCode = apiErrorCode;
        this.apiErrorMessage = LanguageHelper.getLocalizedMessage(apiErrorMessageId, language, parameter0, parameter1);
    }

    public BaseLogicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseLogicalException(Throwable cause) {
        super(cause);
    }
    
    public String getApiErrorMessage() {
    	return apiErrorMessage;
    }
    
    public int getApiErrorCode() {
    	return apiErrorCode;
    }
    
    @Override
    public String toString() {
        return super.toString() + "\n  apiErrorCode=" + apiErrorCode + ", " + apiErrorMessage;
    }
}
