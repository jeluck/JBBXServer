package com.jbb.mgt.core.dao;

import java.util.List;

import com.jbb.mgt.core.domain.Property;
import org.apache.ibatis.annotations.Param;

public interface SystemDao {

    int getPropsVersion();

    List<Property> getSystemProperties();

    String getSystemPropertyValue(String name);

    void saveSystemProperty(String name, String value);

    void increasePropsVersion();

    List<Property> selectSystemPropertiesByName(String name);

    void updateSystemProperty(String name, String value);
}
