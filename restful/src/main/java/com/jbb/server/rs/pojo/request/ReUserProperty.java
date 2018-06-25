package com.jbb.server.rs.pojo.request;

import java.util.List;

import com.jbb.server.core.domain.UserProperty;

public class ReUserProperty {
	private List<UserProperty> properties;
	
	public ReUserProperty() {
	}
	
	public ReUserProperty(List<UserProperty> properties) {
		this.properties = properties;
	}

	public List<UserProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<UserProperty> properties) {
		this.properties = properties;
	}
	
	
}
