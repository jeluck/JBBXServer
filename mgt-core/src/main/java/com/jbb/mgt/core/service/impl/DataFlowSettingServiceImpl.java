package com.jbb.mgt.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.dao.DataFlowSettingDao;
import com.jbb.mgt.core.domain.DataFlowSetting;
import com.jbb.mgt.core.service.DataFlowSettingService;

/**
 * 流量控制service实现类
 * 
 * @author wyq
 * @date 2018/04/27
 */
@Service("DataFlowSettingService")
public class DataFlowSettingServiceImpl implements DataFlowSettingService {

    @Autowired
    private DataFlowSettingDao dataFlowSettingsDao;

    @Override
    public DataFlowSetting getDataFlowSettingByOrgId(int orgId) {
        return dataFlowSettingsDao.selectDataFlowSettingByOrgId(orgId);
    }

    @Override
    public void saveDataFlowSetting(DataFlowSetting dataFlowSetting) {
        dataFlowSettingsDao.saveDataFlowSetting(dataFlowSetting);
    }

    @Override
    public List<DataFlowSetting> getDataFlowSettings() {
        return dataFlowSettingsDao.selectDataFlowSettings();
    }

}
