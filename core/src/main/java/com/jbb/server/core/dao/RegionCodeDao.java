package com.jbb.server.core.dao;

import com.jbb.server.core.domain.Region;

public interface RegionCodeDao {
    Region getRegionByCode(String code);
}
