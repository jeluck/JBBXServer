package com.jbb.server.common.exception;

public class ObjectNotFoundException extends BaseLogicalException {
    private static final long serialVersionUID = -502907558228738051L;
    
    public ObjectNotFoundException() {
        super(OBJECT_NOT_FOUND, null, "zh");
    }
    
    public ObjectNotFoundException(String message) {
    	super(OBJECT_NOT_FOUND, message, "zh");
    }
    
    public ObjectNotFoundException(String messageId, String language){
    	super(OBJECT_NOT_FOUND, messageId, language);
    }
    
    public ObjectNotFoundException(String messageId, String language, String parameter0) {
        super(OBJECT_NOT_FOUND, messageId, language, parameter0);
    }
}
