package com.jbb.server.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.server.core.dao.CompanyIntroductionDao;
import com.jbb.server.core.domain.CompanyIntroduction;
import com.jbb.server.core.service.CompanyIntroductionService;

@Service("CompanyIntroductionService")
public class CompanyIntroductionServiceImpl implements CompanyIntroductionService {
	@Autowired
	private CompanyIntroductionDao companyIntroductionDao;
	@Override
	public CompanyIntroduction getCompanyIntroduction() {
		return companyIntroductionDao.selectCompanyIntroduction();
	}
}
