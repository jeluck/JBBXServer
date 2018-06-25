 package com.jbb.server.core.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.server.core.dao.RegionCodeDao;
import com.jbb.server.core.dao.mapper.RegionCodeMapper;
import com.jbb.server.core.domain.Region;

@Repository("RegionCodeDao")
public class RegionCodeDaoImpl implements RegionCodeDao {

    @Autowired
    private RegionCodeMapper mapper;
    
    @Override
    public Region getRegionByCode(String code) {
        return mapper.selectRegionByCode(code);
    }

}
