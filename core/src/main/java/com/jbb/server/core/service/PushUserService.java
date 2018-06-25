 package com.jbb.server.core.service;

import com.jbb.server.core.domain.User;

public interface PushUserService {

    void postUserData(User user, String serverPath);

}
