package com.jbb.server.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.server.core.dao.SystemDao;
import com.jbb.server.core.dao.mapper.SystemMapper;
import com.jbb.server.core.domain.Property;

@Repository("systemDao")
public class SystemDaoImpl implements SystemDao {

    @Autowired
    private SystemMapper mapper;

    @Override
    public int getPropsVersion() {
        Integer version = mapper.selectPropsVersion();
        return version == null ? 0 : version;
    }

    @Override
    public List<Property> getSystemProperties() {
        return mapper.selectSystemProperties();
    }

    @Override
    public void saveSystemProperty(String name, String value) {
        mapper.saveSystemProperty(name, value);
    }

    @Override
    public String getSystemPropertyValue(String name) {   
        Property p = mapper.selectSystemProperty(name);
        return p!=null?p.getValue():null;
    }

    @Override
    public void increasePropsVersion() {
        mapper.increasePropsVersion();
         
    }

    
    
}
