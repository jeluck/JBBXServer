package com.jbb.mgt.core.dao;

import java.util.List;

import com.jbb.mgt.core.domain.DataFlowSetting;

/**
 * 流量控制dao接口
 * 
 * @author wyq
 * @date 2018/04/27
 */
public interface DataFlowSettingDao {

    void saveDataFlowSetting(DataFlowSetting dataFlowSetting);

    DataFlowSetting selectDataFlowSettingByOrgId(int orgId);
    
    List<DataFlowSetting> selectDataFlowSettings();

}
