package com.jbb.server.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.jbb.server.core.dao.AdvertisingDao;
import com.jbb.server.core.dao.mapper.AdvertisingMapper;
import com.jbb.server.core.domain.Advertising;
@Repository("AdvertisingDao")
public class AdvertisingDaoImpl implements AdvertisingDao {
	@Autowired
	private AdvertisingMapper advertisingMapper;
	@Override
	public Advertising selectAdById(Integer adId) {
		return advertisingMapper.selectAdById(adId);
	}

	@Override
	public List<Advertising> selectAdvertising(String platform) {
		return advertisingMapper.selectAdvertising(platform);
	}

}
