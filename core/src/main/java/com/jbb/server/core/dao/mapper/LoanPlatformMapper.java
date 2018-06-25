package com.jbb.server.core.dao.mapper;

import com.jbb.server.core.domain.LoanPlatform;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface LoanPlatformMapper {

    List<LoanPlatform> selectLoanPlatforms(@Param(value = "userId") Integer userId,
        @Param(value = "loanType") String loanType);
    
    List<LoanPlatform> selectAllLoanPlatforms();

}
