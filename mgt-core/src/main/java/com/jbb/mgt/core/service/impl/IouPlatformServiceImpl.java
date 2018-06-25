package com.jbb.mgt.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.dao.IouPlatformDao;
import com.jbb.mgt.core.domain.IouPlatform;
import com.jbb.mgt.core.service.IouPlatformService;

@Service("IouPlatformService")
public class IouPlatformServiceImpl implements IouPlatformService {
    @Autowired
    private IouPlatformDao iouPlatformDao;

    @Override
    public List<IouPlatform> selectPlatforms() {
        return iouPlatformDao.selectPlatforms();
    }

}
