package com.jbb.server.core.dao;

import java.util.List;

import com.jbb.server.core.domain.FollowerRecord;

public interface IouFollowersDao {

    /**
     * 插入或更新记录
     * 
     * @param userId
     * @param iouCode
     * @param status
     * @return
     */
    public boolean saveIouFollower(int userId, String iouCode, int status);

    /**
     * 查找关注记录
     * 
     * @param userId
     * @param iouCode
     * @param status TODO
     * @return
     */
    public List<FollowerRecord> getIouFollowers(String iouCode);

    public FollowerRecord getIouFollowerByUserId(String iouCode, int userId);

    /**
     * 查询是否已经关注过此借条
     * 
     * @param iouCode
     * @param userId
     * @return
     */
    public Boolean checkExistUserFollowed(String iouCode, int userId);
}
