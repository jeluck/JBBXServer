package com.jbb.server.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.server.core.dao.SystemDao;
import com.jbb.server.core.domain.Property;
import com.jbb.server.core.service.SystemService;

@Service("SystemService")
public class SystemServiceImpl implements SystemService {

    @Autowired
    private SystemDao systemDao;

    @Override
    public int getPropsVersion() {

        return systemDao.getPropsVersion();
    }

    @Override
    public List<Property> getSystemProperties() {
        return systemDao.getSystemProperties();
    }

    @Override
    public void saveSystemProperty(String name, String value) {
        systemDao.increasePropsVersion();
        systemDao.saveSystemProperty(name, value);
    }

    @Override
    public String getSystemPropertyValue(String name) {
        return systemDao.getSystemPropertyValue(name);
    }

}
