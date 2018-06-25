package com.jbb.server.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.core.dao.IouFollowersDao;
import com.jbb.server.core.dao.mapper.IouFollowerMapper;
import com.jbb.server.core.domain.FollowerRecord;

@Repository("IouFollowersDao")
public class IouFollowersDaoImpl implements IouFollowersDao {

    @Autowired
    private IouFollowerMapper mapper;

    @Override
    public boolean saveIouFollower(int userId, String iouCode, int status) {

        return mapper.saveIouFollower(iouCode, userId, status) > 1;
    }

    @Override
    public List<FollowerRecord> getIouFollowers(String iouCode) {

        return mapper.getIouFollowers(iouCode, null);
    }

    @Override
    public FollowerRecord getIouFollowerByUserId(String iouCode, int userId) {
        List<FollowerRecord> list = mapper.getIouFollowers(iouCode, userId);
        if (CommonUtil.isNullOrEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public Boolean checkExistUserFollowed(String iouCode, int userId) {
        return mapper.checkExistUserFollowed(iouCode, userId) == 1;
    }

}
