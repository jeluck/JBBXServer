package com.jbb.server.rs.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.shared.rs.InformationCodes;

@XmlRootElement(name = ActionResponse.ROOT_XML_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ActionResponse implements ApiResponse {
    public static final String ROOT_XML_NAME = "response";
    private int resultCode = InformationCodes.SUCCESS;   
    private String resultCodeMessage = null;
	private Integer collectionTotalSize;
	private Long lockTime;
	@JsonIgnore
	private ApiCall apiCall;

	@Override
    @XmlElement
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
    
    @XmlElement(name="resultCodeDesc")
    public String getResultCodeDesc() {
        int rc = getResultCode();
        return (InformationCodes.CODE_DESCRIPTIONS.length > rc) && (rc > 0) ? InformationCodes.CODE_DESCRIPTIONS[rc] : null;
    }

    @XmlElement
    public String getResultCodeMessage() {
		return resultCodeMessage;
	}
	public void setResultCodeMessage(String resultCodeMessage) {
		this.resultCodeMessage = resultCodeMessage;
	}

	public void setResultCodeAndMessage(int resultCode, String resultCodeMessage) {
		this.resultCode = resultCode;
		this.resultCodeMessage = resultCodeMessage;
	}

	public void escalateErrorCode(int forceErrorCode) {
        if (this.resultCode == InformationCodes.SUCCESS) {
        	this.resultCode = forceErrorCode;
        }
    }

    @XmlElement(name="collectionTotalSize")
    public Integer getCollectionTotalSize() {
        return collectionTotalSize;
    }

    public void setCollectionTotalSize(Integer collectionTotalSize) {
        this.collectionTotalSize = collectionTotalSize;
    }
    
    @Override
    public ApiCall getApiCall() {
        return apiCall;
    }
    public void setApiCall(ApiCall apiCall) {
        this.apiCall = apiCall;
    }

    @XmlElement
    public Long getLockTime() {
        return lockTime;
    }

    public void setLockTime(Long lockTime) {
        this.lockTime = lockTime;
    }
}
