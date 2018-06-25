package com.jbb.mgt.core.dao.mapper;

import com.jbb.mgt.core.domain.DataYxUrls;
import org.apache.ibatis.annotations.Param;

/**
 * 二维码有效期mapper类
 * 
 * @author wyq
 * @date 2018/6/1 12:00
 */
public interface DataYxUrlsMapper {
    DataYxUrls selectDataYxUrlsByUserIdAndReportType(@Param(value = "userId") int userId,
        @Param(value = "reportType") String reportType);

    void insertDataYxUrls(DataYxUrls dataYxUrls);

    void updateDataYxUrls(DataYxUrls dataYxUrls);
}
