package com.jbb.mgt.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.DataFlowSettingDao;
import com.jbb.mgt.core.dao.mapper.DataFlowSettingMapper;
import com.jbb.mgt.core.domain.DataFlowSetting;

/**
 * 流量控制dao实现类
 * 
 * @author wyq
 * @date 2018/04/27
 */
@Repository("DataFlowSettingDao")
public class DataFlowSettingDaoImpl implements DataFlowSettingDao {

    @Autowired
    private DataFlowSettingMapper mapper;

    @Override
    public void saveDataFlowSetting(DataFlowSetting dataFlowSetting) {
        try {
            mapper.insertDataFlowSetting(dataFlowSetting);
        } catch (DuplicateKeyException e) {
            mapper.updateDataFlowSetting(dataFlowSetting);
        }

    }

    @Override
    public DataFlowSetting selectDataFlowSettingByOrgId(int orgId) {
        return mapper.selectDataFlowSettingByOrgId(orgId);
    }

    @Override
    public List<DataFlowSetting> selectDataFlowSettings() {
        return mapper.selectDataFlowSettings();
    }

}
