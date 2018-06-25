 package com.jbb.mgt.core.service;

import com.jbb.mgt.core.domain.AreaZones;

public interface AreaZonesService {
     /**
      * 根据前六位查询户籍
      * 
      * @param zone
      */
     AreaZones selectAreazonesByZone(String zone);
}
