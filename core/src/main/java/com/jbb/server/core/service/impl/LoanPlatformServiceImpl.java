package com.jbb.server.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.server.core.dao.LoanPlatformDao;
import com.jbb.server.core.domain.LoanPlatform;
import com.jbb.server.core.service.LoanPlatformService;

@Service("LoanPlatformService")
public class LoanPlatformServiceImpl implements LoanPlatformService {
    @Autowired
    private LoanPlatformDao loanPlatformDao;

    @Override
    public List<LoanPlatform> getLoanPlatforms(Integer userId, String loanType) {
        return loanPlatformDao.selectLoanPlatforms(userId, loanType);
    }

    @Override
    public List<LoanPlatform> getAllLoanPlatforms() {
         return loanPlatformDao.selectAllLoanPlatforms();
    }

}
