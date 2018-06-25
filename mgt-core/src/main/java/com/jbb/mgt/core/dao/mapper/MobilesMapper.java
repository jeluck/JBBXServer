 package com.jbb.mgt.core.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.Mobiles;

public interface MobilesMapper {

    void insertMobiles(Mobiles mobiles);

    List<Mobiles> selectMobiles(@Param(value = "phoneNumber")String phoneNumber,@Param(value = "checkType") String checkType);

}
