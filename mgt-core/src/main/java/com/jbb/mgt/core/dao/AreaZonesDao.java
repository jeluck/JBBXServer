package com.jbb.mgt.core.dao;

import com.jbb.mgt.core.domain.AreaZones;

public interface AreaZonesDao {
    /**
     * 根据前六位查询户籍
     * 
     * @param zone
     */
    AreaZones selectAreazonesByZone(String zone);
}
