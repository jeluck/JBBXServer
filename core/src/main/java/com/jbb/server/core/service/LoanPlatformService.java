package com.jbb.server.core.service;

import java.util.List;

import com.jbb.server.core.domain.LoanPlatform;

public interface LoanPlatformService {
    
    List<LoanPlatform> getLoanPlatforms(Integer userId, String loanType);
    
    List<LoanPlatform> getAllLoanPlatforms();
    
    
}
