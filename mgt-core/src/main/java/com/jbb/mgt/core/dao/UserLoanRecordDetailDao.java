package com.jbb.mgt.core.dao;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.mgt.core.domain.Money;
import com.jbb.mgt.core.domain.UserLoanRecordDetail;
import org.apache.ibatis.annotations.Param;

public interface UserLoanRecordDetailDao {

    void insertUserLoanRecordDetail(UserLoanRecordDetail loanRecordDetail);

    List<UserLoanRecordDetail> selectUserLoanRecordDetails(int loanId);

    /**
     * 按机构编号查询统计金额数据
     * 
     * @param orgId
     * @param startDate
     * @param endDate
     * @return
     */
    Money selectCountMoneyByOrgId(Integer orgId, Timestamp startDate, Timestamp endDate);


    /**
     * 查询累计待收金额
     *
     * @param orgId
     * @return
     */
    int selectDueMoney(Integer orgId);

    /**
     * 查询借条金额
     *
     * @param orgId
     * @param startDate
     * @param endDate
     * @return
     */
    int selectBorrowingMoney(Integer orgId, Timestamp startDate, Timestamp endDate);
}
