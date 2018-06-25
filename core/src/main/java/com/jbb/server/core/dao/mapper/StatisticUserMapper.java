package com.jbb.server.core.dao.mapper;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.LenderCounts;
import com.jbb.server.core.domain.SourceCounts;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserCounts;

public interface StatisticUserMapper {

    List<UserCounts> countUserBySourceId(@Param("sourceId") String sourceId, @Param("dateStr") String dateStr);

    List<User> selectRecommandUsersBySourceId(@Param("sourceId") String sourceId, @Param("dateStr") String dateStr);
    
    List<User> selectRecommandUsersBySourceIds(@Param("sourceIds") String[] sourceIds, @Param("dateStr") String dateStr);

    List<UserCounts> countUserBySourceIds(@Param("sourceIds") String[] sourceIds, @Param("dateStr") String dateStr);

    List<LenderCounts> countUsersForLender(@Param("start") Timestamp start, @Param("targetUserId") Integer targetUserId);

    List<SourceCounts> countUsersForSource(@Param("start") Timestamp start, @Param("sourceIds") String[] sourceIds);

    List<Integer> selectTargetUserIds(@Param("userId") int userId);
    
    int countUsersByLendId(@Param("targetUserId") int targetUserId, @Param("start") Timestamp start, @Param("end") Timestamp end);

}
