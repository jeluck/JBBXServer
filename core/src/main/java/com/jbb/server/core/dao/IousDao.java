package com.jbb.server.core.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.Iou;
import com.jbb.server.core.domain.IousAmountStatistic;

public interface IousDao {
    /**
     * 增加借条信息
     * 
     * @param iou
     * @return
     */
    boolean insertIou(Iou iou);

    /**
     * 获取借条信息
     * 
     * @param iouCode
     * @return
     */
    Iou getIouByCode(String iouCode);

    /**
     * 修改借条信息
     * 
     * @param iou
     * @return
     */
    boolean updateIou(Iou iou);

    /**
     * 修改借条状态：0发布，1生效，3修改待出借人确认，4修改且出借人确认，5延期申请，6延期申请确认，7已还申请，8已还申请确认
     * 
     * @param iouCode
     * @param status
     * @return
     */
    boolean updateIouStatus(String iouCode, int status);

    /**
     * 删除借条信息
     * 
     * @param iou
     * @param role
     * @return
     */
    boolean deleteIouForLender(String iouCode);

    boolean deleteIouForBorrower(String iouCode);

    List<Iou> searchIousForHall(String lastIouCode, Integer forward, int pageSize);

    List<Iou> selectFollowedIous(int userId);

    List<Iou> selectLendIous(int userId);

    List<Iou> selectPublishedIous(int userId);

    int countHallNewIous(Timestamp start);

    int countFollowUpdatedIous(int userId, Timestamp start);

    int countBorrowOrLendUpdatedIous(Integer borrowerId, Integer lenderId, Timestamp start);

    List<Iou> selectIntendIous(int lenderId, List<Integer> statuses, Boolean order);

    int countIousByStatus(int borrowerId, int[] statuses);

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
    List<Iou> selectRecentIousByBorrowerId(int borrowerId, Timestamp currentDate, boolean feature, int size,
        int[] statuses, int[] excludStatuses);

    /**
     * 统计借条借入借出总额、利息、次数
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
     * 筛选借条
     * @param statues: 状态列表
     * @param iouCode：借条ID
     * @param userName：姓名
     * @param phoneNumber：手机号
     * @param lenderId：出借人ID
     * @param borrowerId: 借款人ID
     * @param lastIouCode: 借条ID
     * @param forward: 刷新或者获取历史 
     * @param pageSize：返回条数
     * @return
     */
    List<Iou> selectIous(int[] statuses, int[] filterStatuses,  String searchText, String iouCode, String userName, String phoneNumber, 
        Integer lenderId, Integer borrowerId, String lastIouCode, Integer amountMin, 
        Integer amountMax, 
        Timestamp borrowingStart, 
        Timestamp borrowingEnd, 
        Timestamp repaymentStart, 
        Timestamp repaymentEnd, 
        Integer forward,
        int pageSize);
    
    
    /**
     * @param statues
     * @param pageSize
     * @param messageType 
     * @return
     */
    List<Iou> selectIousForMessage(int[] statues, int pageSize, int messageType, Timestamp sendDate, Timestamp startDate,Timestamp endDate);
    
    
    
   /**
    * 检查是否可以更改当前的借条状态
    * @param preStatus
    * @param newStatus
    * @param lenderCtrl
    * @param borrowerCtrl
    * @return
    */
    boolean checkRightToUpdateStatus(int preStatus, int newStatus, boolean lenderCtrl, boolean borrowerCtrl);
    
    boolean insertIouAlertRecord(String iouCode,int messageType,Timestamp creationDate);
}
