package com.jbb.server.core.dao.mapper;

import org.apache.ibatis.annotations.Param;

public interface UserPrivMapper {
    /**
     * 插入或者更新一条记录
     * @param applyUserId
     * @param userId
     * @param privName
     * @param privValue
     * @return
     */
    int saveUserPriv(@Param("applyUserId")int applyUserId, @Param("userId")int userId, 
        @Param("privName")String privName, 
        @Param("privValue")boolean privValue);
    
    /**
     * 检查是否存在权限
     * @param applyUserId
     * @param userId
     * @param privName
     * @return
     */
    int checkUserPrivByPrivName(@Param("applyUserId")int applyUserId, @Param("userId")int userId, 
        @Param("privName")String privName);
   
}
