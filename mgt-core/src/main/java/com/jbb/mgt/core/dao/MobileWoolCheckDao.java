package com.jbb.mgt.core.dao;

import com.jbb.mgt.core.domain.ChuangLanWoolCheck;

public interface MobileWoolCheckDao {

    void insertWoolCheckResult(ChuangLanWoolCheck data);

    /**
     * 获取最近一次
     * 
     * @param mobile
     */
    ChuangLanWoolCheck getWoolCheckResult(String mobile);

}
