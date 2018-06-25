package com.jbb.mgt.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.DataFlowBaseDao;
import com.jbb.mgt.core.dao.mapper.DataFlowBaseMapper;
import com.jbb.mgt.core.domain.DataFlowBase;

/**
 * 流量基本信息dao实现类
 * 
 * @author wyq
 * @date 2018/04/27
 */
@Repository("DataFlowBaseDao")
public class DataFlowBaseDaoImpl implements DataFlowBaseDao {

    @Autowired
    private DataFlowBaseMapper mapper;

    @Override
    public void insertDataFlowBase(DataFlowBase dataFlowBase) {
        mapper.insertDataFlowBase(dataFlowBase);
    }

    @Override
    public DataFlowBase selectDataFlowConfigById(int dataFlowId) {
        return mapper.selectDataFlowConfigById(dataFlowId);
    }

    @Override
    public List<DataFlowBase> selectDataFlowBase() {

        return mapper.selectDataFlowBase();
    }

    @Override
    public void updateDataFlowBase(DataFlowBase dataFlowBase) {
        mapper.updateDataFlowBase(dataFlowBase);

    }

    @Override
    public boolean deleteDataFlowBaseByDataFlowId(int DataFlowId) {
        return mapper.deleteDataFlowBase(DataFlowId) > 0;
    }

}
