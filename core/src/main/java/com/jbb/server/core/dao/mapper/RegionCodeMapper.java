package com.jbb.server.core.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.Region;

public interface RegionCodeMapper {

    Region selectRegionByCode(@Param("code") String code);

}
