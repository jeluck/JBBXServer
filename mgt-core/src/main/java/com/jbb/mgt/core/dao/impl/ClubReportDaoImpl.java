package com.jbb.mgt.core.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.ClubReportDao;
import com.jbb.mgt.core.dao.mapper.ClubReportMapper;
import com.jbb.mgt.core.domain.ClubReport;

@Repository("ClubReportDao")
public class ClubReportDaoImpl implements ClubReportDao {
    @Autowired
    private ClubReportMapper mapper;

    @Override
    public void insertReport(ClubReport report) {
        mapper.insertReport(report);
    }

    @Override
    public ClubReport selectReport(Integer userId, String identityCode, String realName, String channelType,
        String username, String taskId) {
        return mapper.selectReport(userId, identityCode, realName, channelType, username, taskId);
    }

    @Override
    public void saveReport(ClubReport report) {
        try {
            mapper.insertReport(report);
        } catch (DuplicateKeyException e) {
            mapper.updateReport(report);
        }

    }

}
