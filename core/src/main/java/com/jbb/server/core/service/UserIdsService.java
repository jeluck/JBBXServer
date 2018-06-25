package com.jbb.server.core.service;

import java.util.List;

import com.jbb.server.core.domain.User;

public interface UserIdsService {
	List<User> getUserHeadAndNickName(List<Integer> userIds);
}
