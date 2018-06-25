package com.jbb.mgt.core.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.YxReport;

public interface YxReportMapper {
    
    void saveReportInfo(YxReport report);

    YxReport selectReport(@Param(value = "userId") Integer userId, 
        @Param(value = "applyId") Integer applyId, @Param(value = "taskId") String taskId, 
        @Param(value = "reportType") String reportType);
}
