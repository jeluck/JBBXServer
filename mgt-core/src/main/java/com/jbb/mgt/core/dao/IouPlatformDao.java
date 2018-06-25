package com.jbb.mgt.core.dao;

import java.util.List;

import com.jbb.mgt.core.domain.IouPlatform;

public interface IouPlatformDao {
    List<IouPlatform> selectPlatforms();
}
