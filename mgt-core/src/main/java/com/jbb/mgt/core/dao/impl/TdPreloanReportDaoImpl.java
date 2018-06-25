package com.jbb.mgt.core.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.TdPreloanReportDao;
import com.jbb.mgt.core.dao.mapper.TdPreloanReportMapper;
import com.jbb.mgt.core.domain.PreloanReport;

@Repository("TdPreloanReportDao")
public class TdPreloanReportDaoImpl implements TdPreloanReportDao {

    @Autowired
    private TdPreloanReportMapper mapper;

    @Override
    public void insertPreloanReport(PreloanReport report) {
        mapper.insertPreloanReport(report);
    }

    @Override
    public void updatePreloanReport(PreloanReport report) {
        mapper.updatePreloanReport(report);
    }

    @Override
    public PreloanReport selectPreloanReportByApplyId(int applyId) {
        return mapper.selectPreloanReport(null, null, applyId);
    }
    
    @Override
    public PreloanReport selectPreloanReportByReportId(String reportId) {
        return mapper.selectPreloanReport(reportId, null, null);
    }
    
    
    @Override
    public PreloanReport selectPreloanReportByUserId(int userId) {
        return mapper.selectPreloanReport(null, userId, null);
    }

}
