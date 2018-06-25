package com.jbb.server.core.service;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.IntendRecord;
import com.jbb.server.core.domain.Iou;
import com.jbb.server.core.domain.IouIntention;
import com.jbb.server.core.domain.IousAmountStatistic;

public interface IousService {

    int countIntentionalUsers(String iouCode);

    void insertIou(Iou iou);

    /**
     * 查询一条借条记录
     * 
     * @param iouCode
     * @return
     */
    Iou getIouByIouCode(String iouCode);

    /**
     * 修改借条
     * 
     * @param iou
     */
    void updateIou(Iou iou, Integer intentionUserId);

    /**
     * 查询大厅的借条列表
     * 
     * @return
     */
    List<Iou> getIousForHall(String lastIouCode, Integer forward, int size);

    /**
     * 查询用户关注的借条列表
     * 
     * @return
     */
    List<Iou> getFollowedIousByUserId(int userId);

    /**
     * 查询我出借的借条信息
     * 
     * @param lenderId
     * @return
     */
    List<Iou> getLendIousByLenderId(int lenderId);

    /**
     * 查询我发布的借条信息
     * 
     * @param borrowerId
     * @return
     */
    List<Iou> getPublishedIousByBorrowerId(int borrowerId);

    /**
     * 修改关注状态
     * 
     * @param userId
     * @param iouCode
     */
    void updateIouFollowStatus(int userId, String iouCode, int newStatus);

    /**
     * 修改意向状态
     * 
     * @param userId
     * @param iouCode
     */
    void updateIouIntentionalStatus(int userId, String iouCode, int newStatus);

    void rejectIntentionalUsers(String iouCode, List<Integer> userIds, Integer excludeUserId);

    /**
     * 查询意向出借人
     * 
     * @param iouCode
     * @return
     */
    List<IntendRecord> getIntentionUsers(String iouCode);

    /**
     * 查询用户是否存在意向
     * 
     * @param iouCode
     * @return
     */
    boolean checkExistUserIntention(String iouCode, int userId);

    /**
     * 检查是否已经关注
     * 
     * @param iouCode
     * @param userId
     * @return
     */
    Boolean checkExistUserFollowed(String iouCode, int userId);

    boolean deleteIouForLender(String iouCode);

    boolean deleteIouForBorrower(String iouCode);

    int countHallNewIous(Timestamp start);

    int countFollowUpdatedIous(int userId, Timestamp start);

    int countBorrowOrLendUpdatedIous(Integer borrowerId, Integer lenderId, Timestamp start);

    IouIntention getIntentionByUserId(String iouCode, int userId);

    List<Iou> getRecentIousForBorrower(int borrowerId);

    int countIousByStatuses(int borrowerId, int[] statuses);

    /**
     * 统计借入、借出信息
     * 
     * @param statuses
     * @param borrowerId
     * @param lenderId
     * @param start
     * @param end
     * @return
     */
    IousAmountStatistic statisticIousAmountAndCnt(int[] statuses, Integer borrowerId, Integer lenderId, Timestamp start,
        Timestamp end);

    /**
     * @param borrowerId
     * @param lenderId
     * @param lastIouCode
     * @param forward
     * @param size
     * @return
     */
    List<Iou> getIouFillList(Integer borrowerId, Integer lenderId, String lastIouCode, Integer forward, int size);

    List<Iou> selectIous(int[] statuses, int[] filterStatuses, String searchText, String iouCode, String userName,
        String phoneNumber, Integer lenderId, Integer borrowerId, String lastIouCode, Integer amountMin,
        Integer amountMax, Timestamp borrowingStart, Timestamp borrowingEnd, Timestamp repaymentStart,
        Timestamp repaymentEnd, Integer forward, int pageSize);

    /**
     * @param statues
     * @param pageSize
     * @param
     * @return
     */
    List<Iou> selectIousForMessage(int[] statues, int pageSize, int messageType, Timestamp sendDate,
        Timestamp startDate, Timestamp endDate);
}
