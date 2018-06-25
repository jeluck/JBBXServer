package com.jbb.mgt.core.dao;

import com.jbb.mgt.core.domain.YxReport;

public interface YxReportDao {

    void saveReportInfo(YxReport report);

    YxReport selectReport(Integer userId, Integer applyId, String taskId, String reportType);

}
