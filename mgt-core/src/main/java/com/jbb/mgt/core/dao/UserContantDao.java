package com.jbb.mgt.core.dao;

import com.jbb.mgt.core.domain.UserContant;

import java.util.List;

public interface UserContantDao {

    void insertOrUpdateUserContant(List<UserContant> userContants);

    List<UserContant> selectUserContantByUserId(Integer userId);

    void jbbToMgtInsertOrUpdateUserContant(List<UserContant> userContants,Integer userId);
}
