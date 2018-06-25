package com.jbb.server.core.dao;

import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.Users;

public interface UsersDao {

    int insertUser(User user);

    int insertSelective(Users record);

    Users selectmobile(String moblie);
}
