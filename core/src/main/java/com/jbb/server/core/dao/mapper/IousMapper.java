package com.jbb.server.core.dao.mapper;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.Iou;
import com.jbb.server.core.domain.IousAmountStatistic;

public interface IousMapper {
    /**
     * 增加借条信息
     * 
     * @param iou
     * @return
     */
    int insertIou(Iou iou);

    /**
     * 修改借条信息
     * 
     * @param iou
     * @return
     */
    int updateIou(Iou iou);

    /**
     * 修改借条信息
     * 
     * @param iouCode
     * @return
     */
    Iou selectIouByCode(String iouCode);

    /**
     * 借条状态：0发布，1生效，2修改申请，3出借人确认修改，4出借人拒绝修改，5申请延期，6出借人确认延期，7出借人拒绝延期，8申请已还，9出借人确认已还，10出借人拒绝已还
     * 
     * @param iouCode
     * @param status
     * @return
     */
    int updateIouStatus(@Param("iouCode") String iouCode, @Param("status") int status);

    /**
     * 删除借条信息
     * 
     * @param iou
     * @return
     */
    int deleteIouForLender(@Param("iouCode") String iouCode);

    int deleteIouForBorrower(@Param("iouCode") String iouCode);

    List<Iou> searchIousForHall(@Param("lastIouCode") String lastIouCode, @Param("forward") Integer forward,
        @Param("pageSize") int pageSize);

    /**
     * 查询借条列表
     * 
     * @param lastIouCode
     * @param forward
     * @param borrowerId
     * @param lenderId
     * @param status
     * @param order
     * @return
     */
    List<Iou> searchIous(@Param("lastIouCode") String lastIouCode, @Param("forward") Integer forward,
        @Param("borrowerId") Integer borrowerId, @Param("lenderId") Integer lenderId, @Param("status") Integer status,
        @Param("order") Boolean order);

    /**
     * 检索出借意向的借条
     * 
     * @param lenderId
     * @param statuses
     * @param order
     * @return
     */
    List<Iou> searchIntendIous(@Param("lenderId") int lenderId, @Param("statuses") List<Integer> statuses,
        @Param("order") Boolean order);

    /**
     * 查询关注列表
     * 
     * @param userId
     * @return
     */
    List<Iou> selectFollowedIous(@Param("userId") int userId);

    int countHallNewIous(@Param("start") Timestamp start);

    int countBorrowOrLendUpdatedIous(@Param("borrowerId") Integer borrowerId, @Param("lenderId") Integer lenderId,
        @Param("start") Timestamp start);

    int countFollowUpdatedIous(@Param("userId") int userId, @Param("start") Timestamp start);

    int countIousByStatus(@Param("borrowerId") int borrowerId, @Param("statuses") int[] statuses);

    /**
     * 获取借款人当前借条列表
     * 
     * @param borrowerId
     * @param currentDate
     * @param feature
     * @param statuses
     * @param excludStatuses
     * @return
     */
    List<Iou> selectRecentIousByBorrowerId(@Param("borrowerId") int borrowerId,
        @Param("currentDate") Timestamp currentDate, @Param("feature") boolean feature, @Param("size") int size,
        @Param("statuses") int[] statuses, @Param("excludStatuses") int[] excludStatuses);

    /**
     * 统计借条借入、借出数据
     * 
     * @param statuses
     * @param borrowerId
     * @param lenderId
     * @param start
     * @param end
     * @return
     */
    IousAmountStatistic statisticIousAmountAndCnt(@Param("statuses") int[] statuses,
        @Param("borrowerId") Integer borrowerId, @Param("lenderId") Integer lenderId, @Param("start") Timestamp start,
        @Param("end") Timestamp end);

    /**
     * 筛选借条
     * 
     * @param statues
     * @param phoneNumber
     * @param iouCode
     * @param userName
     * @param lenderId
     * @param borrowerId
     * @param lastIouCode
     * @param forward
     * @param pageSize
     * @return
     */
    List<Iou> selectIous(@Param("statuses") int[] statues, @Param("filterStatuses") int[] filterStatuses,
        @Param("searchText") String searchText, @Param("phoneNumber") String phoneNumber,
        @Param("iouCode") String iouCode, @Param("userName") String userName, @Param("lenderId") Integer lenderId,
        @Param("borrowerId") Integer borrowerId, @Param("amountMin") Integer amountMin,
        @Param("amountMax") Integer amountMax, @Param("borrowingStart") Timestamp borrowingStart,
        @Param("borrowingEnd") Timestamp borrowingEnd, @Param("repaymentStart") Timestamp repaymentStart,
        @Param("repaymentEnd") Timestamp repaymentEnd, @Param("lastIouCode") String lastIouCode,
        @Param("forward") Integer forward, @Param("pageSize") int pageSize);

    List<Iou> selectIousForMessage(@Param("statuses") int[] statues, @Param("pageSize") int pageSize,
        @Param("messageType") int messageType, @Param("sendDate") Timestamp sendDate,
        @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    int checkRightToUpdateStatus(@Param("preStatus") int preStatus, @Param("newStatus") int newStatus,
        @Param("lenderCtrl") boolean lenderCtrl, @Param("borrowerCtrl") boolean borrowerCtrl);

    int insertIouAlertRecord(@Param("iouCode") String iouCode, @Param("messageType") int messageType,
        @Param("creationDate") Timestamp creationDate);

}
