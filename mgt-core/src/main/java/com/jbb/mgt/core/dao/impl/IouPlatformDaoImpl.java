package com.jbb.mgt.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.IouPlatformDao;
import com.jbb.mgt.core.dao.mapper.IouPlatformMapper;
import com.jbb.mgt.core.domain.IouPlatform;

@Repository("IouPlatformDao")
public class IouPlatformDaoImpl implements IouPlatformDao {

    @Autowired
    private IouPlatformMapper mapper;

    @Override
    public List<IouPlatform> selectPlatforms() {
        return mapper.selectPlatforms();
    }

}
