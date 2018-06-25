 package com.jbb.mgt.core.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.ChuangLanWoolCheck;

public interface MobileWoolCheckMapper {

    void insertWoolCheckResult(ChuangLanWoolCheck data);

    ChuangLanWoolCheck getWoolCheckResult(@Param(value = "mobile")String mobile);

}
