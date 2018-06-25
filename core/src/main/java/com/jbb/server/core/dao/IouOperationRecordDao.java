package com.jbb.server.core.dao;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.server.core.domain.IouOperationRecord;

public interface IouOperationRecordDao {

    int insertIouOperationRecord(IouOperationRecord iouOperationRecords);

    List<IouOperationRecord> searchIouOperationRecords(String iouCode, Integer fromUser, Integer toUser,
        String opName, Timestamp opDate);
    
   
}
