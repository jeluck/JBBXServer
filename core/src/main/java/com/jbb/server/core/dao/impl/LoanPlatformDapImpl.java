package com.jbb.server.core.dao.impl;

import com.jbb.server.core.dao.LoanPlatformDao;
import com.jbb.server.core.dao.mapper.LoanPlatformMapper;
import com.jbb.server.core.domain.LoanPlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("LoanPlatformDao")
public class LoanPlatformDapImpl implements LoanPlatformDao {
    @Autowired
    private LoanPlatformMapper loanPlatformMapper;

    @Override
    public List<LoanPlatform> selectLoanPlatforms(Integer userId, String loanType) {

        return loanPlatformMapper.selectLoanPlatforms(userId, loanType);
    }

    @Override
    public List<LoanPlatform> selectAllLoanPlatforms() {
        return loanPlatformMapper.selectAllLoanPlatforms();
    }

}
