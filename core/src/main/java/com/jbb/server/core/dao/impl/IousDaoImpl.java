package com.jbb.server.core.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.server.core.dao.IousDao;
import com.jbb.server.core.dao.mapper.IousMapper;
import com.jbb.server.core.domain.Iou;
import com.jbb.server.core.domain.IousAmountStatistic;

@Repository("IousDao")
public class IousDaoImpl implements IousDao {

    @Autowired
    private IousMapper iousMapper;

    @Override
    public boolean insertIou(Iou iou) {
        return iousMapper.insertIou(iou) > 0;
    }

    @Override
    public boolean updateIou(Iou iou) {
        return iousMapper.updateIou(iou) > 0;
    }

    @Override
    public Iou getIouByCode(String iouCode) {
        return iousMapper.selectIouByCode(iouCode);
    }

    /**
     * 借条状态：0发布，1生效，2修改申请，3出借人确认修改，4出借人拒绝修改，5申请延期，6出借人确认延期，7出借人拒绝延期，8申请已还，9出借人确认已还，10出借人拒绝已还
     */
    @Override
    public boolean updateIouStatus(String iouCode, int status) {
        return iousMapper.updateIouStatus(iouCode, status) > 0;
    }

    @Override
    public boolean deleteIouForLender(String iouCode) {
        return iousMapper.deleteIouForLender(iouCode) > 0;
    }

    @Override
    public boolean deleteIouForBorrower(String iouCode) {
        return iousMapper.deleteIouForBorrower(iouCode) > 0;
    }

    @Override
    public List<Iou> searchIousForHall(String lastIouCode, Integer forward, int pageSize) {
        return iousMapper.searchIousForHall(lastIouCode, forward, pageSize);
    }

    @Override
    public List<Iou> selectFollowedIous(int userId) {
        return iousMapper.selectFollowedIous(userId);
    }

    @Override
    public List<Iou> selectLendIous(int lenderId) {
        return iousMapper.searchIous(null, null, null, lenderId, null, null);
    }

    @Override
    public List<Iou> selectIntendIous(int lenderId, List<Integer> statuses, Boolean order) {
        return iousMapper.searchIntendIous(lenderId, statuses, order);
    }

    @Override
    public List<Iou> selectPublishedIous(int borrowerId) {
        return iousMapper.searchIous(null, null, borrowerId, null, null, null);
    }

    @Override
    public int countHallNewIous(Timestamp start) {

        return iousMapper.countHallNewIous(start);
    }

    @Override
    public int countBorrowOrLendUpdatedIous(Integer borrowerId, Integer lenderId, Timestamp start) {
        return iousMapper.countBorrowOrLendUpdatedIous(borrowerId, lenderId, start);
    }

    @Override
    public int countFollowUpdatedIous(int userId, Timestamp start) {
        return iousMapper.countFollowUpdatedIous(userId, start);
    }

    @Override
    public List<Iou> selectRecentIousByBorrowerId(int borrowerId, Timestamp currentDate, boolean feature, int size,
        int[] statuses, int[] excludStatuses) {
        return iousMapper.selectRecentIousByBorrowerId(borrowerId, currentDate, feature, size, statuses,
            excludStatuses);
    }

    @Override
    public int countIousByStatus(int borrowerId, int[] statuses) {
        return iousMapper.countIousByStatus(borrowerId, statuses);
    }

    @Override
    public IousAmountStatistic statisticIousAmountAndCnt(int[] statuses, Integer borrowerId, Integer lenderId,
        Timestamp start, Timestamp end) {
        return iousMapper.statisticIousAmountAndCnt(statuses, borrowerId, lenderId, start, end);
    }

    @Override
    public List<Iou> selectIous(int[] statues, int[] filterStatuses, String searchText, String iouCode, String userName,
        String phoneNumber, Integer lenderId, Integer borrowerId, String lastIouCode, Integer amountMin,
        Integer amountMax, Timestamp borrowingStart, Timestamp borrowingEnd, Timestamp repaymentStart,
        Timestamp repaymentEnd, Integer forward, int pageSize) {

        return iousMapper.selectIous(statues, filterStatuses, searchText, phoneNumber, iouCode, userName, lenderId,
            borrowerId, amountMin, amountMax, borrowingStart, borrowingEnd, repaymentStart, repaymentEnd, lastIouCode,
            forward, pageSize);
    }

    @Override
    public boolean checkRightToUpdateStatus(int preStatus, int newStatus, boolean lenderCtrl, boolean borrowerCtrl) {
        return iousMapper.checkRightToUpdateStatus(preStatus, newStatus, lenderCtrl, borrowerCtrl) > 0;
    }

    @Override
    public List<Iou> selectIousForMessage(int[] statues, int pageSize, int messgeType, Timestamp sendDate, Timestamp startDate,
        Timestamp endDate) {
        return iousMapper.selectIousForMessage(statues, pageSize, messgeType, sendDate, startDate, endDate);
    }

    @Override
    public boolean insertIouAlertRecord(String iouCode, int messageType, Timestamp creationDate) {
        return iousMapper.insertIouAlertRecord(iouCode, messageType, creationDate) > 0;
    }

}
