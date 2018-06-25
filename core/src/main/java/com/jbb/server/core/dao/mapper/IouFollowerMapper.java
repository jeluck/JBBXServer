package com.jbb.server.core.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.FollowerRecord;

public interface IouFollowerMapper {

    /**
     * 插入或者更新follower
     * 
     * @param iouCode
     * @param userId
     * @param stauts
     * @return
     */
    int saveIouFollower(@Param("iouCode") String iouCode, @Param("userId") int userId, @Param("status") int status);

    /**
     * 获取follwer记录
     * 
     * @param iouCode
     * @param userId
     * @return
     */
    List<FollowerRecord> getIouFollowers(@Param("iouCode") String iouCode, @Param("userId") Integer userId);

    /**
     * 查找是否关注过此借条
     * 
     * @param iouCode
     * @param userId
     * @return
     */
    int checkExistUserFollowed(@Param("iouCode") String iouCode, @Param("userId") int userId);
}
