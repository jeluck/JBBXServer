package com.jbb.mgt.core.service;

import com.jbb.mgt.core.domain.YxNotify;
import com.jbb.mgt.core.domain.YxReport;
import com.jbb.mgt.core.domain.YxReportRsp;

public interface YxReportService {
    
    String generateH5Url(int userId, int applyId, String reportType);
    
    void notify(YxNotify notifyInfo);
    
    void saveReportInfo(YxReport report);

    YxReport getReport(Integer userId, Integer applyId, String taskId, String reportType);
    
    YxReportRsp getReportFormYx(String token);

}
