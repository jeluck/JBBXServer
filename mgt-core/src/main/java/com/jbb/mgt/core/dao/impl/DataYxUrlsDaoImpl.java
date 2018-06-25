package com.jbb.mgt.core.dao.impl;

import com.jbb.mgt.core.dao.DataYxUrlsDao;
import com.jbb.mgt.core.dao.mapper.DataYxUrlsMapper;
import com.jbb.mgt.core.domain.DataYxUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 二维码有效期dao接口实现类
 *
 * @author wyq
 * @date 2018/6/1 14:12
 */
@Repository("DataYxUrlsDao")
public class DataYxUrlsDaoImpl implements DataYxUrlsDao {

    @Autowired
    private DataYxUrlsMapper mapper;

    @Override
    public DataYxUrls selectDataYxUrlsByUserIdAndReportType(int userId, String reportType) {
        return mapper.selectDataYxUrlsByUserIdAndReportType(userId, reportType);
    }

    @Override
    public void insertDataYxUrls(DataYxUrls dataYxUrls) {
        mapper.insertDataYxUrls(dataYxUrls);
    }

    @Override
    public void updateDataYxUrls(DataYxUrls dataYxUrls) {
        mapper.updateDataYxUrls(dataYxUrls);
    }
}
