package com.jbb.server.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.server.core.dao.AdvertisingDao;
import com.jbb.server.core.domain.Advertising;
import com.jbb.server.core.service.AdvertisingService;
@Service("AdvertisingService")
public class AdvertisingServiceImpl implements AdvertisingService {
	@Autowired
	private AdvertisingDao advertisingDao;
	@Override
	public Advertising getAdById(Integer adId) {
		return advertisingDao.selectAdById(adId);
	}

	@Override
	public List<Advertising> getAdvertising(String Platform) {
		return advertisingDao.selectAdvertising(Platform);
	}

}
