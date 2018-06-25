package com.jbb.mgt.core.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.ClubReport;

public interface ClubReportMapper {

    void insertReport(ClubReport report);

    ClubReport selectReport(@Param(value = "userId") Integer userId, @Param(value = "identityCode") String identityCode,
        @Param(value = "realName") String realName, @Param(value = "channelType") String channelType,
        @Param(value = "username") String username, @Param(value = "taskId") String taskId);

    void updateReport(ClubReport report);


}
