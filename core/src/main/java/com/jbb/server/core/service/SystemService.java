package com.jbb.server.core.service;

import java.util.List;

import com.jbb.server.core.domain.Property;

public interface SystemService {
    int getPropsVersion();

    List<Property> getSystemProperties();

    void saveSystemProperty(String name, String value);

    String getSystemPropertyValue(String name);
}
