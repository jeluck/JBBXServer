package com.jbb.mgt.core.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.Property;

public interface SystemMapper {

    void saveSystemProperty(@Param("name") String name, @Param("value") String value);

    List<Property> selectSystemProperties();

    Integer selectPropsVersion();

    Property selectSystemProperty(@Param("name") String name);

    void increasePropsVersion();

    List<Property> selectSystemPropertiesByName(@Param("name") String name);

    void updateSystemProperty(@Param("name") String name, @Param("value") String value);
}
