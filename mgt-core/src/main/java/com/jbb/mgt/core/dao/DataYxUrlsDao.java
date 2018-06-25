package com.jbb.mgt.core.dao;

import com.jbb.mgt.core.domain.DataYxUrls;
import org.apache.ibatis.annotations.Param;

/**
 * 二维码有效期dao接口
 * 
 * @author wyq
 * @date 2018/6/1 14:11
 */
public interface DataYxUrlsDao {
    DataYxUrls selectDataYxUrlsByUserIdAndReportType(@Param(value = "userId") int userId,
        @Param(value = "reportType") String reportType);

    void insertDataYxUrls(DataYxUrls dataYxUrls);

    void updateDataYxUrls(DataYxUrls dataYxUrls);
}
