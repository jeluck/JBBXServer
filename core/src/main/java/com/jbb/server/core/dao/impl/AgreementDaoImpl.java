package com.jbb.server.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.server.core.dao.AgreementDao;
import com.jbb.server.core.dao.mapper.AgreementMapper;
import com.jbb.server.core.domain.Agreement;

@Repository("AgreementDao")
public class AgreementDaoImpl implements AgreementDao {

    @Autowired
    private AgreementMapper agreementMapper;

    @Override
    public int insertAgreement(int userId, String agreement, String version) {
        return agreementMapper.insertAgreement(userId, agreement, version);
    }

    @Override
    public List<Agreement> searchAgreement(int userId, String agreement, String version) {
        return agreementMapper.searchAgreement(userId, agreement, version);
    }

}
