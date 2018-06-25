package com.jbb.mgt.core.service;

import java.util.List;

import com.jbb.mgt.core.domain.Property;
import org.apache.ibatis.annotations.Param;

public interface SystemService {
    int getPropsVersion();

    List<Property> getSystemProperties();

    void saveSystemProperty(String name, String value);

    String getSystemPropertyValue(String name);

    List<Property> selectSystemPropertiesByName(String name);

    void updateSystemProperty(String name, String value);
}
