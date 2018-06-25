package com.jbb.mgt.core.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.PreloanReport;

public interface TdPreloanReportMapper {

    void insertPreloanReport(PreloanReport report);

    void updatePreloanReport(PreloanReport report);
    
    PreloanReport selectPreloanReport(@Param(value="reportId")String reportId, @Param(value="userId")Integer userId, @Param(value="applyId")Integer applyId);

}
