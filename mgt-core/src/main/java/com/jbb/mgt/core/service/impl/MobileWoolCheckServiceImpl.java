 package com.jbb.mgt.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.dao.MobileWoolCheckDao;
import com.jbb.mgt.core.domain.ChuangLanWoolCheck;
import com.jbb.mgt.core.service.MobileWoolCheckService;
@Service("MobileWoolCheckService")
public class MobileWoolCheckServiceImpl implements MobileWoolCheckService {

    private static Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);
    @Autowired
    private MobileWoolCheckDao mobileWoolCheckDao;
    @Override
    public void insertWoolCheckResult(ChuangLanWoolCheck data) {
        mobileWoolCheckDao.insertWoolCheckResult(data);
    }

    @Override
    public ChuangLanWoolCheck getWoolCheckResult(String mobile) {
        return mobileWoolCheckDao.getWoolCheckResult(mobile);
    }

}
