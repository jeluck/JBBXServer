package com.jbb.server.core.dao;

import com.jbb.server.core.domain.LoanPlatform;

import java.util.List;

public interface LoanPlatformDao {
    /**
     * 按类型查询借款平台
     * 
     * @param userId ： 如果有值，按用户检索是否借过平台
     * @param loanType：类型:hot , matched, new
     * @return
     */
    List<LoanPlatform> selectLoanPlatforms(Integer userId, String loanType);

    List<LoanPlatform> selectAllLoanPlatforms();
}
