package com.jbb.server.core.dao;

import java.util.List;

import com.jbb.server.core.domain.Advertising;

public interface AdvertisingDao {
    Advertising selectAdById(Integer adId);

    List<Advertising> selectAdvertising(String platform);
}
