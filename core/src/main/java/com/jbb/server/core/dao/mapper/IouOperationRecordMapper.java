package com.jbb.server.core.dao.mapper;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.IouOperationRecord;

public interface IouOperationRecordMapper {
    /**
     * 插入一条操作记录
     * @param operationRecord
     * @return
     */
    int insertIouOperationRecord(IouOperationRecord iouOperationRecord);
    
    /**
     * 查询操作
     * @param iouCode
     * @param fromUserId
     * @param toUserId
     * @param operationName
     * @param operationDate
     * @return
     */
    List<IouOperationRecord> searchIouOperationRecords(@Param("iouCode") String iouCode,
        @Param("fromUser") Integer fromUser, @Param("toUser") Integer toUser,
        @Param("opName") String opName, @Param("opDate")Timestamp opDate);
}
