package com.jbb.server.core.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.Advertising;

public interface AdvertisingMapper {
    Advertising selectAdById(Integer adId);
    List<Advertising> selectAdvertising(@Param("platform") String platform);
}