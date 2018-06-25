package com.jbb.server.core.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.IntendRecord;
import com.jbb.server.core.domain.IouIntention;

public interface IouIntentionalUserMapper {

    /**
     * 统计出借意向用户数
     * 
     * @param iouCod
     * @return
     */
    int countIntentionalUsers(@Param("iouCode") String iouCod);

    /**
     * 获取意向出借人
     * 
     * @param iouCode
     * @return
     */
    List<IntendRecord> getIntentionalUsers(@Param("iouCode") String iouCode, @Param("userId") Integer userId);

    /**
     * 插入或者更新记录
     * 
     * @param userId
     * @param iouCode
     * @param status
     * @return
     */
    int saveIouIntentional(@Param("userId") int userId, @Param("iouCode") String iouCode, @Param("status") int status);

    /**
     * 拒绝意向出借人
     * 
     * @param iouCode
     * @param userIds
     */
    void rejectIouIntentionalUsers(@Param("iouCode") String iouCode, @Param("userIds") List<Integer> userIds,
        @Param("excludeUserId") Integer excludeUserId);

    /**
     * 检查用户是否存在意向
     * 
     * @param iouCode
     * @param userId
     * @return
     */
    int checkExistUserIntention(@Param("iouCode") String iouCode, @Param("userId") int userId);

    /**
     * 
     * @param iouCode
     * @param userId
     * @return
     */
    IouIntention selectIntentionByUserId(@Param("iouCode") String iouCode, @Param("userId") int userId);

}
