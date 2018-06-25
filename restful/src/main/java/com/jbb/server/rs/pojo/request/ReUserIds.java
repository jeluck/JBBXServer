package com.jbb.server.rs.pojo.request;

import java.util.List;

public class ReUserIds {
	private List<Integer> userIds;

	public ReUserIds() {
	}

	public ReUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}

	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}

	@Override
	public String toString() {
		return "ReUserIds [userIds=" + userIds + "]";
	}

}
