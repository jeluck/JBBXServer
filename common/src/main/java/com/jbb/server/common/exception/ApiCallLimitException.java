 package com.jbb.server.common.exception;

 public class ApiCallLimitException extends BaseLogicalException {

  
    private static final long serialVersionUID = -8901544529186421168L;
    
    public ApiCallLimitException() {
        super(API_LIMIT, "jbb.error.exception.api.limit", "zh");
    }
    
    public ApiCallLimitException(String messageId) {
        super(API_LIMIT, messageId, "zh");
    }
    
    public ApiCallLimitException(String messageId, String parameter0) {
        super(API_LIMIT, messageId, "zh", parameter0);
    }
    
  

}
