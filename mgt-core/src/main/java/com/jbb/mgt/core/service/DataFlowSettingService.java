package com.jbb.mgt.core.service;

import java.util.List;

import com.jbb.mgt.core.domain.DataFlowSetting;

/**
 * 流量控制service接口
 * 
 * @author wyq
 * @date 2018/04/27
 */
public interface DataFlowSettingService {
   
    DataFlowSetting getDataFlowSettingByOrgId(int orgId);

    void saveDataFlowSetting(DataFlowSetting dataFlowSetting);
    
    List<DataFlowSetting> getDataFlowSettings();

}
