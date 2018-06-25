package com.jbb.server.core.dao;

import java.util.List;

import com.jbb.server.core.domain.IntendRecord;
import com.jbb.server.core.domain.IouIntention;

public interface IouIntentionalUserDao {
    /**
     * 统计出借意向用户数
     * 
     * @param iouCode
     * @return
     */
    int countIntentionalUsers(String iouCode);

    /**
     * 查询意向出借人
     * 
     * @param iouCode
     * @return
     */
    List<IntendRecord> getIntentionalUsers(String iouCode);

    IntendRecord getIntentionalUsersByUserId(String iouCode, int userId);

    /**
     * 插入或者更新状态
     * 
     * @param userId
     * @param iouCode
     * @param status
     * @return
     */
    boolean saveIouIntentional(int userId, String iouCode, int status);

    /**
     * rejectIouIntentionalUsers
     * 
     * @param iouCode
     * @param userIds
     */
    void rejectIouIntentionalUsers(String iouCode, List<Integer> userIds, Integer excludeUserId);

    /**
     * 查询用户对借条是否存在意向
     * 
     * @param iouCode
     * @param userId
     * @return
     */
    boolean checkExistUserIntention(String iouCode, int userId);

    IouIntention getIntentionByUserId(String iouCode, int userId);
}
