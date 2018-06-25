 package com.jbb.mgt.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.dao.AreaZonesDao;
import com.jbb.mgt.core.domain.AreaZones;
import com.jbb.mgt.core.service.AreaZonesService;

@Service("AreaZonesService")
public class AreaZonesServiceImpl implements AreaZonesService{

    private static Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);
    
    @Autowired
    private AreaZonesDao areaZonesDao;
    
    @Override
    public AreaZones selectAreazonesByZone(String zone) {
         return areaZonesDao.selectAreazonesByZone(zone);
    }

}
