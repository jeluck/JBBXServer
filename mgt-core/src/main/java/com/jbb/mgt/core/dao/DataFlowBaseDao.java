package com.jbb.mgt.core.dao;

import java.util.List;

import com.jbb.mgt.core.domain.DataFlowBase;

/**
 * 流量基本信息dao接口
 * 
 * @author wyq
 * @date 2018/04/27
 */
public interface DataFlowBaseDao {

    void insertDataFlowBase(DataFlowBase dataFlowBase);

    DataFlowBase selectDataFlowConfigById(int dataFlowId);

    List<DataFlowBase> selectDataFlowBase();

    void updateDataFlowBase(DataFlowBase dataFlowBase);

    boolean deleteDataFlowBaseByDataFlowId(int dataFlowId);
}
