package com.jbb.server.core.dao;

import java.util.List;

import com.jbb.server.core.domain.Property;

public interface SystemDao {

    int getPropsVersion();

    List<Property> getSystemProperties();
    
    String getSystemPropertyValue(String name);

    void saveSystemProperty(String name, String value);

    void increasePropsVersion();

}
