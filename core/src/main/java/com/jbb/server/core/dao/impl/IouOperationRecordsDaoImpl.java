package com.jbb.server.core.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.server.core.dao.IouOperationRecordDao;
import com.jbb.server.core.dao.mapper.IouOperationRecordMapper;
import com.jbb.server.core.domain.IouOperationRecord;

@Repository("IouOperationRecordsDao")
public class IouOperationRecordsDaoImpl implements IouOperationRecordDao {

    @Autowired
    private IouOperationRecordMapper operationRecordsMapper;

    @Override
    public int insertIouOperationRecord(IouOperationRecord iouOperationRecord) {
        return operationRecordsMapper.insertIouOperationRecord(iouOperationRecord);
    }

    @Override
    public List<IouOperationRecord> searchIouOperationRecords(String iouCode, Integer fromUser, Integer toUser,
        String opName, Timestamp opDate) {
        return operationRecordsMapper.searchIouOperationRecords(iouCode, toUser, toUser, opName,
            opDate);
    }

}
