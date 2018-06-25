package com.jbb.server.core.service;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.server.core.domain.LenderCounts;
import com.jbb.server.core.domain.Region;
import com.jbb.server.core.domain.SourceCounts;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserCounts;
import com.jbb.server.core.domain.UserProperty;

public interface UserService {
	
	 User selectUserByApplyId(int applyId);

    boolean checkExistPropertyAndNotNull(List<UserProperty> properties, String name);

    boolean chekcIdCardRegion(String idcard);

    Region getRegionByIdCard(String idcard);

    List<UserCounts> statisticUser(String sourceId, String dateStr);

    List<User> getRecommandUsersBySourceId(String sourceId, String dateStr);

    List<User> getRecommandUsersBySourceIds(String[] sourceIds, String dateStr);

    List<UserCounts> statisticUser(String[] sourceIds, String dateStr);

    List<LenderCounts> countUsersForLender(Timestamp start, Integer targetUserId);

    List<SourceCounts> countUsersForSource(Timestamp start, String[] sourceIds);
    
    List<Integer> getTargetUserIds(int userId);

}
