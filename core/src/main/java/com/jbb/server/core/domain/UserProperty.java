package com.jbb.server.core.domain;

public class UserProperty {
    
    
    public static String P_SERVER_PATH = "postDateServerPath";
    public static String P_BEAN_CLASS = "postDateClassName";
    
    public static String P_CHANNEL_SOURCES = "sources";
    public static String P_CHANNEL_MAX_CNT = "maxCount";
    public static String P_CHANNEL_WEIGHT = "weight";
    public static String P_CHANNEL_FILTER_EXPRESSION = "filterExpression";

    
	private String name;
	private String value;

	public UserProperty() {
	}

	public UserProperty(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public UserProperty(int userId, String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String toString() {
		return "UserProperty [name=" + name + ", value=" + value + "]";
	}
}
