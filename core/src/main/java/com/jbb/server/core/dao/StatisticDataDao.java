package com.jbb.server.core.dao;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.server.core.domain.LenderCounts;
import com.jbb.server.core.domain.SourceCounts;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserCounts;

public interface StatisticDataDao {
    List<UserCounts> statisticUserBySourceId(String sourceId, String dateStr);

    List<User> getRecommandUsersBySourceId(String sourceId, String dateStr);

    List<UserCounts> statisticUserBySourceIds(String[] sourceIds, String dateStr);

    List<User> getRecommandUsersBySourceIds(String[] sourceIds, String dateStr);

    List<LenderCounts> countUsersForLender(Timestamp start, Integer targetUserId);

    List<SourceCounts> countUsersForSource(Timestamp start, String[] sourceIds);

    List<Integer> getTargetUserIds(int userId);

    int countUsersByLendId(int targetUserId, Timestamp start, Timestamp end);

}
