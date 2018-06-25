package com.jbb.mgt.core.dao;

import com.jbb.mgt.core.domain.PreloanReport;

public interface TdPreloanReportDao {

    void insertPreloanReport(PreloanReport report);

    void updatePreloanReport(PreloanReport report);
    
    PreloanReport selectPreloanReportByApplyId(int applyId);
    
    PreloanReport selectPreloanReportByReportId(String reportId);

    PreloanReport selectPreloanReportByUserId(int userId);
    
}
