package com.jbb.mgt.core.service.impl;

import com.jbb.mgt.core.dao.DataYxUrlsDao;
import com.jbb.mgt.core.domain.DataYxUrls;
import com.jbb.mgt.core.service.DataYxUrlsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 二维码有效期Service接口实现类
 *
 * @author wyq
 * @date 2018/6/1 14:17
 */
@Service("DataYxUrlsService")
public class DataYxUrlsServiceImpl implements DataYxUrlsService {

    @Autowired
    private DataYxUrlsDao dao;

    @Override
    public DataYxUrls selectDataYxUrlsByUserIdAndReportType(int userId, String reportType) {
        return dao.selectDataYxUrlsByUserIdAndReportType(userId, reportType);
    }

    @Override
    public void insertDataYxUrls(DataYxUrls dataYxUrls) {
        dao.insertDataYxUrls(dataYxUrls);
    }

    @Override
    public void updateDataYxUrls(DataYxUrls dataYxUrls) {
        dao.updateDataYxUrls(dataYxUrls);
    }
}
