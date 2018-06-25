package com.jbb.server.core.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.dao.RegionCodeDao;
import com.jbb.server.core.dao.StatisticDataDao;
import com.jbb.server.core.domain.LenderCounts;
import com.jbb.server.core.domain.Region;
import com.jbb.server.core.domain.SourceCounts;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserCounts;
import com.jbb.server.core.domain.UserProperty;
import com.jbb.server.core.service.UserService;

@Service("UserService")
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private RegionCodeDao regionCodeDao;

    @Autowired
    private StatisticDataDao statisticDataDao;
    
    @Autowired
    private UserDao userDao;

    @Override
    public boolean checkExistPropertyAndNotNull(List<UserProperty> properties, String name) {
        if (CommonUtil.isNullOrEmpty(properties) || StringUtil.isEmpty(name)) {
            return false;
        }
        for (UserProperty p : properties) {
            if (p.getName().equals(name) && !StringUtil.isEmpty(p.getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean chekcIdCardRegion(String idcard) {
        Region region = this.getRegionByIdCard(idcard);
        return region != null;
    }

    @Override
    public Region getRegionByIdCard(String idcard) {
        String code = idcard.substring(0, 6);
        return regionCodeDao.getRegionByCode(code);
    }

    @Override
    public List<UserCounts> statisticUser(String sourceId, String dateStr) {
        if (logger.isDebugEnabled()) {
            logger.debug(">statisticUser, scourceId=" + sourceId);
        }
        List<UserCounts> userCnts = statisticDataDao.statisticUserBySourceId(sourceId, dateStr);
        logger.debug("<statisticUser");
        return userCnts;
    }

    @Override
    public List<UserCounts> statisticUser(String[] sourceIds, String dateStr) {
        if (logger.isDebugEnabled()) {
            logger.debug(">statisticUser, sourceIds=" + sourceIds);
        }
        List<UserCounts> userCnts = statisticDataDao.statisticUserBySourceIds(sourceIds, dateStr);
        logger.debug("<statisticUser");
        return userCnts;
    }

    @Override
    public List<User> getRecommandUsersBySourceId(String sourceId, String dateStr) {
        if (logger.isDebugEnabled()) {
            logger.debug(">getRecommandUsersBySourceId, scourceId=" + sourceId);
        }
        List<User> users = statisticDataDao.getRecommandUsersBySourceId(sourceId, dateStr);
        logger.debug("<statisticUser");
        return users;
    }

    @Override
    public List<User> getRecommandUsersBySourceIds(String[] sourceIds, String dateStr) {
        if (logger.isDebugEnabled()) {
            logger.debug(">getRecommandUsersBySourceId, sourceIds=" + sourceIds);
        }
        List<User> users = statisticDataDao.getRecommandUsersBySourceIds(sourceIds, dateStr);
        logger.debug("<statisticUser");
        return users;
    }

    @Override
    public List<LenderCounts> countUsersForLender(Timestamp start, Integer targetUserId) {
        return statisticDataDao.countUsersForLender(start, targetUserId);
    }

    @Override
    public List<SourceCounts> countUsersForSource(Timestamp start, String[] sourceIds) {
        return statisticDataDao.countUsersForSource(start, sourceIds);
    }

    @Override
    public List<Integer> getTargetUserIds(int userId) {
         return statisticDataDao.getTargetUserIds(userId);
    }

	@Override
	public User selectUserByApplyId(int applyId) {
		// TODO Auto-generated method stub
		return null;
	}

    
}
