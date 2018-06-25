package com.jbb.mgt.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.dao.SystemDao;
import com.jbb.mgt.core.domain.Property;
import com.jbb.mgt.core.service.SystemService;

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

    @Override
    public List<Property> selectSystemPropertiesByName(String name) {
        return systemDao.selectSystemPropertiesByName(name);
    }

    @Override
    public void updateSystemProperty(String name, String value) {
        systemDao.updateSystemProperty(name, value);
    }

}
