package com.jbb.server.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.core.dao.IouIntentionalUserDao;
import com.jbb.server.core.dao.mapper.IouIntentionalUserMapper;
import com.jbb.server.core.domain.IntendRecord;
import com.jbb.server.core.domain.IouIntention;

@Repository("IouIntentionalUsersDao")
public class IouIntentionalUserDaoImpl implements IouIntentionalUserDao {

    @Autowired
    private IouIntentionalUserMapper mapper;

    @Override
    public int countIntentionalUsers(String iouCode) {
        return mapper.countIntentionalUsers(iouCode);
    }

    @Override
    public List<IntendRecord> getIntentionalUsers(String iouCode) {
        return mapper.getIntentionalUsers(iouCode, null);
    }

    @Override
    public IntendRecord getIntentionalUsersByUserId(String iouCode, int userId) {
        List<IntendRecord> list = mapper.getIntentionalUsers(iouCode, userId);
        if (CommonUtil.isNullOrEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public boolean checkExistUserIntention(String iouCode, int userId) {
        return mapper.checkExistUserIntention(iouCode, userId) > 0;
    }

    @Override
    public boolean saveIouIntentional(int userId, String iouCode, int status) {
        return mapper.saveIouIntentional(userId, iouCode, status) > 0;
    }

    @Override
    public void rejectIouIntentionalUsers(String iouCode, List<Integer> userIds, Integer excludeUserId) {
        mapper.rejectIouIntentionalUsers(iouCode, userIds, excludeUserId);
    }

    @Override
    public IouIntention getIntentionByUserId(String iouCode, int userId) {
        return mapper.selectIntentionByUserId(iouCode, userId);
    }

}
