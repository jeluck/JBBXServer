package com.jbb.server.core.dao;

import java.util.List;

import com.jbb.server.core.domain.Agreement;

public interface AgreementDao {
    
   /**
    * 插入一条协议记录
    * @param userId
    * @param agreement
    * @param version
    * @return
    */
    int insertAgreement(int userId,String agreement,String version);

    /**
     * 查询协议阅读记录
     * @param userId
     * @param agreement
     * @param version
     * @return
     */
    List<Agreement> searchAgreement(int userId,String agreement,String version);
}
