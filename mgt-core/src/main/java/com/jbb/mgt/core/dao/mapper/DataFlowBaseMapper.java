package com.jbb.mgt.core.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.DataFlowBase;

/**
 * 流量基本信息Mapper接口
 * 
 * @author Administrator
 * @date 2018/04/27
 */
public interface DataFlowBaseMapper {

    void insertDataFlowBase(DataFlowBase dataFlowBase);

    DataFlowBase selectDataFlowConfigById(@Param(value = "dataFlowId") int DataFlowId);
    
    List<DataFlowBase> selectDataFlowBase();

    void updateDataFlowBase(DataFlowBase dataFlowBase);

    int deleteDataFlowBase(@Param(value = "dataFlowId") int DataFlowId);
}
