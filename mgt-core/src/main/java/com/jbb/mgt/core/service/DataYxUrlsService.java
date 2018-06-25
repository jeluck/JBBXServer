package com.jbb.mgt.core.service;

import com.jbb.mgt.core.domain.DataYxUrls;
import org.apache.ibatis.annotations.Param;

/**
 * 二维码有效期Service接口
 *
 * @author wyq
 * @date 2018/6/1 14:16
 */
public interface DataYxUrlsService {
    DataYxUrls selectDataYxUrlsByUserIdAndReportType(@Param(value = "userId") int userId,
                                                     @Param(value = "reportType") String reportType);

    void insertDataYxUrls(DataYxUrls dataYxUrls);

    void updateDataYxUrls(DataYxUrls dataYxUrls);
}
