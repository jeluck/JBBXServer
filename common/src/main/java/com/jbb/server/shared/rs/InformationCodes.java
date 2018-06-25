package com.jbb.server.shared.rs;

public interface InformationCodes {
	// SUCCESS CODES
    int SUCCESS = 0;
	
	// ERROR CODES
    
    // internal error
    int FAILURE = 1;
    
    int WRONG_API_KEY = 2;
    int ACCESS_DENIED = 3;
    int WRONG_PARAM_VALUE = 4;
    int MISSED_PARAMETER = 5;
    int PARSING_ERROR = 6;
    int OBJECT_NOT_FOUND = 7;
    int SERVICE_UNAVAILABLE = 8;
    int EXTERNAL_APPLICATION_ERROR = 9;
    int METHOD_NOT_FOUND = 10;
    int REQUEST_TIMEOUT = 11;
    int ALIYUN_REQUEST_ERROR = 12;
    int DUPLICATE_ENTITY = 13;
    int WRONG_IOUCODE = 14;
    int WRONG_IOU_INFO = 15;
    int API_LIMIT = 17;
    int WX_PAY_ERROR = 18;
    int JBB_ERROR = 19;
    int ALIPAY_ERROR = 20;
    int CHUANGLAN_REQUEST_ERROR=21;

    
    String[] CODE_DESCRIPTIONS = {
            null,
            "Internal error",
            "Wrong User API Key",
            "Access denied",
            "Wrong parameter value",
            "Missed mandatory parameter value",
            "Error in parsing of input data",
            "Object not found",
            "Service is temporary unavailable",
            "Third party application error",
            "Requested API method not available",
            "Request timeout",
            "Call Aliyun Api error",
            "Duplicate entity or property",
            "Wrong iouCode",
            "Wrong iou information",
            "Call api limit",
            "Weixin Pay Exception",
            "JBB Cloud Exception",
            "AliPay Exception",
            "Call Chuanglan Api error"
    };
}
