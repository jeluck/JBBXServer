package com.jbb.server.core.service;

import com.jbb.server.core.domain.User;

public interface PaService {

    void postUserToPa(User user, String remoteAddress);
}
