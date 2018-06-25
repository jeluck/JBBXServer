package com.jbb.server.core.dao;

import java.util.List;

import com.jbb.server.core.domain.UserVerifyResult;

public interface UserVerifyDao {

    void saveUserVerifyResult(int userId, String verifyType, int verifyStep, boolean verified);

    boolean checkUserVerified(int userId, String verifyType, int verifyStep);
    
    List<UserVerifyResult> selectUserVerifyResult(int userId);

}
