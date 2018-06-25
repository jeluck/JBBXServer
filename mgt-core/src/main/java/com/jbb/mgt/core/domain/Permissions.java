package com.jbb.mgt.core.domain;
public class Permissions  {
	
	private Integer permissionId;
	private String description;

	public Integer getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	@Override
	public String toString() {
		return "Permissions [permissionId=" + permissionId + ", description=" + description + "]";
	}

}
