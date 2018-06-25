package com.jbb.server.core.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.server.core.dao.StatisticDataDao;
import com.jbb.server.core.dao.mapper.StatisticUserMapper;
import com.jbb.server.core.domain.LenderCounts;
import com.jbb.server.core.domain.SourceCounts;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserCounts;

@Repository("StatisticDataDao")
public class StatisticDataDaoImal implements StatisticDataDao {

    @Autowired
    private StatisticUserMapper mapper;

    @Override
    public List<UserCounts> statisticUserBySourceId(String sourceId, String dateStr) {
        return mapper.countUserBySourceId(sourceId, dateStr);
    }

    @Override
    public List<User> getRecommandUsersBySourceId(String sourceId, String dateStr) {
        return mapper.selectRecommandUsersBySourceId(sourceId, dateStr);
    }

    @Override
    public List<User> getRecommandUsersBySourceIds(String[] sourceIds, String dateStr) {
        return mapper.selectRecommandUsersBySourceIds(sourceIds, dateStr);
    }

    @Override
    public List<UserCounts> statisticUserBySourceIds(String[] sourceIds, String dateStr) {
        return mapper.countUserBySourceIds(sourceIds, dateStr);
    }

    @Override
    public List<LenderCounts> countUsersForLender(Timestamp start, Integer targetUserId) {
        return mapper.countUsersForLender(start, targetUserId);
    }

    @Override
    public List<SourceCounts> countUsersForSource(Timestamp start, String[] sourceIds) {
        return mapper.countUsersForSource(start, sourceIds);
    }

    @Override
    public List<Integer> getTargetUserIds(int userId) {
         return mapper.selectTargetUserIds(userId);
    }

    @Override
    public int countUsersByLendId(int targetUserId, Timestamp start, Timestamp end) {
         return mapper.countUsersByLendId(targetUserId, start, end);
    }

    
}
