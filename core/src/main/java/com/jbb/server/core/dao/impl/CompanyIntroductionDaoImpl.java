package com.jbb.server.core.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.server.core.dao.CompanyIntroductionDao;
import com.jbb.server.core.dao.mapper.CompanyIntroductionMapper;
import com.jbb.server.core.domain.CompanyIntroduction;

@Repository("CompanyIntroductionDao")
public class CompanyIntroductionDaoImpl implements CompanyIntroductionDao{
    @Autowired
    private CompanyIntroductionMapper companyIntroductionMapper;
    @Override
    public CompanyIntroduction selectCompanyIntroduction() {
        return companyIntroductionMapper.selectCompanyIntroduction();
    }
}
