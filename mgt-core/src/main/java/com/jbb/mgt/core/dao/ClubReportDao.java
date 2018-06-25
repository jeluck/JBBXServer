package com.jbb.mgt.core.dao;

import com.jbb.mgt.core.domain.ClubReport;

public interface ClubReportDao {
    
    void insertReport(ClubReport report);
    
    void saveReport(ClubReport report);

    ClubReport selectReport(Integer userId, String identityCode, String realName, String channelType, String username,String taskId);
}
