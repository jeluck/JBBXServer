 package com.jbb.mgt.core.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.AreaZones;

public interface AreaZonesMapper {
     AreaZones selectAreazonesByZone(@Param("zone")String zone);
}
