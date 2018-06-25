package com.jbb.server.core.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.Agreement;

public interface AgreementMapper {
    
   /**
    * 插入一条协议记录
    * @param userId
    * @param agreement
    * @param version
    * @return
    */
    int insertAgreement(@Param("userId") int userId, @Param("agreement") String agreement,
        @Param("version") String version);

    /**
     * 查询协议阅读记录
     * @param userId
     * @param agreement
     * @param version
     * @return
     */
    List<Agreement> searchAgreement(@Param("userId") int userId, @Param("agreement") String agreement,
        @Param("version") String version);
}
