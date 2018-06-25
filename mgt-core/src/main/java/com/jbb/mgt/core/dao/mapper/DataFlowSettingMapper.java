package com.jbb.mgt.core.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.DataFlowSetting;

/**
 * 流量控制Mapper接口
 * 
 * @author
 * @date 2018/04/27
 */
public interface DataFlowSettingMapper {

    void insertDataFlowSetting(DataFlowSetting dataFlowSetting);

    DataFlowSetting selectDataFlowSettingByOrgId(@Param(value = "orgId") int orgId);

    void updateDataFlowSetting(DataFlowSetting dataFlowSetting);
    
    List<DataFlowSetting> selectDataFlowSettings();
}
