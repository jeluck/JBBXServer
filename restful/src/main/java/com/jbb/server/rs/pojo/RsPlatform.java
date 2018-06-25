package com.jbb.server.rs.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.core.domain.LoanPlatform;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsPlatform {
    private int platformid;
    private String name;
    private String description;

    public RsPlatform() {}

    public RsPlatform(LoanPlatform loanPlatforms) {
        this.platformid = loanPlatforms.getPlatformId();
        this.name = loanPlatforms.getName();
        this.description = loanPlatforms.getDescription();
        }

	public int getPlatformid() {
		return platformid;
	}

	public void setPlatformid(int platformid) {
		this.platformid = platformid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
