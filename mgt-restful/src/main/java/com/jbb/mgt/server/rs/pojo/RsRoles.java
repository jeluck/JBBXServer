package com.jbb.mgt.server.rs.pojo;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.Roles;

/**
 * 角色表response实体类
 * 
 * @author wyq
 * @date 2018/04/28
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsRoles {

	private int roleId;
	private String description;
	private List<Integer> permissions;

	public RsRoles(Roles roles,List<Integer> list) {
		this.roleId=roles.getRoleId();
		this.description=roles.getDescription();
		this.permissions=list;
	}
	
	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Integer> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Integer> permissions) {
		this.permissions = permissions;
	}

	public RsRoles() {
		super();
	}

}
