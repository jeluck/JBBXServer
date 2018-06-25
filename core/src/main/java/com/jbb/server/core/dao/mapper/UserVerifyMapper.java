package com.jbb.server.core.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.UserVerifyResult;

public interface UserVerifyMapper {

    int saveUserVerifyResult(@Param("userId") int userId, @Param("verifyType") String verifyType,
        @Param("verifyStep") int verifyStep, @Param("verified") boolean verified);

    int checkUserVerified(@Param("userId") int userId, @Param("verifyType") String verifyType,
        @Param("verifyStep") int verifyStep);

    List<UserVerifyResult> selectUserVerifyResult(@Param("userId") int userId);

}
