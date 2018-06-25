package com.jbb.server.core.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbb.server.common.Constants;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.dao.IouFollowersDao;
import com.jbb.server.core.dao.IouIntentionalUserDao;
import com.jbb.server.core.dao.IousDao;
import com.jbb.server.core.domain.IntendRecord;
import com.jbb.server.core.domain.Iou;
import com.jbb.server.core.domain.IouIntention;
import com.jbb.server.core.domain.IousAmountStatistic;
import com.jbb.server.core.service.IouMessageProcessService;
import com.jbb.server.core.service.IousService;

@Service("IousService")
public class IousServiceImpl implements IousService {

    @Autowired
    private IousDao iousDao;
    @Autowired
    private IouFollowersDao iouFollowersDao;
    @Autowired
    private IouIntentionalUserDao iousIntentionalUserDao;

    @Autowired
    private IouMessageProcessService iouMessageProcessService;

    @Override
    public int countIntentionalUsers(String iouCode) {
        return iousIntentionalUserDao.countIntentionalUsers(iouCode);
    }

    public void insertIou(Iou iou) {
        String iouCode = StringUtil.generateIoUId();
        iou.setIouCode(iouCode);
        iousDao.insertIou(iou);

        // 发送消息
        new Thread(() -> {
            iouMessageProcessService.sendIouMessage(-1, iou);
        }).start();
    }

    @Override
    public Iou getIouByIouCode(String iouCode) {
        return iousDao.getIouByCode(iouCode);
    }

    @Override
    public List<Iou> getIousForHall(String lastIouCode, Integer forward, int pageSize) {
        return iousDao.searchIousForHall(lastIouCode, forward, pageSize);
    }

    @Override
    public List<Iou> getFollowedIousByUserId(int userId) {
        return iousDao.selectFollowedIous(userId);
    }

    @Override
    public List<Iou> getLendIousByLenderId(int lenderId) {
        List<Iou> list = iousDao.selectLendIous(lenderId);
        List<Integer> statuses = new ArrayList<Integer>();
        statuses.add(0); // 有出借意向
        statuses.add(1); // 出借意向被拒
        statuses.add(3); // 出借意向被同意
        statuses.add(4); // 出借人确认修改
        statuses.add(5); // 出借人拒绝修改
        List<Iou> list2 = iousDao.selectIntendIous(lenderId, statuses, null);
        list.addAll(list2);
        return list;
    }

    @Override
    public List<Iou> getPublishedIousByBorrowerId(int borrowerId) {
        return iousDao.selectPublishedIous(borrowerId);
    }

    @Override
    public void updateIouFollowStatus(int userId, String iouCode, int newStatus) {
        iouFollowersDao.saveIouFollower(userId, iouCode, newStatus);
    }

    @Override
    public void updateIouIntentionalStatus(int userId, String iouCode, int newStatus) {
        iousIntentionalUserDao.saveIouIntentional(userId, iouCode, newStatus);
    }

    @Override
    public void rejectIntentionalUsers(String iouCode, List<Integer> userIds, Integer excludeUserId) {
        iousIntentionalUserDao.rejectIouIntentionalUsers(iouCode, userIds, excludeUserId);
    }

    @Override
    public List<IntendRecord> getIntentionUsers(String iouCode) {
        return iousIntentionalUserDao.getIntentionalUsers(iouCode);
    }

    @Override
    public boolean checkExistUserIntention(String iouCode, int userId) {
        return iousIntentionalUserDao.checkExistUserIntention(iouCode, userId);
    }

    @Override
    public Boolean checkExistUserFollowed(String iouCode, int userId) {
        return iouFollowersDao.checkExistUserFollowed(iouCode, userId);
    }

    /**
     * 申请修改，status==>2
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateIou(Iou iou, Integer intentionUserId) {
        iou.setStatus(2);
        iousDao.updateIou(iou);
        if (intentionUserId != null) {
            // 确认意向人
            iousIntentionalUserDao.saveIouIntentional(intentionUserId, iou.getIouCode(), Constants.IOU_INTEND_APPROVE);
            // 拒绝其他单身人
            iousIntentionalUserDao.rejectIouIntentionalUsers(iou.getIouCode(), null, intentionUserId);
        } else {
            // 拒绝所有人
            iousIntentionalUserDao.rejectIouIntentionalUsers(iou.getIouCode(), null, null);
        }
    }

    @Override
    public boolean deleteIouForLender(String iouCode) {
        return iousDao.deleteIouForLender(iouCode);
    }

    @Override
    public boolean deleteIouForBorrower(String iouCode) {
        return iousDao.deleteIouForBorrower(iouCode);
    }

    @Override
    public int countHallNewIous(Timestamp start) {
        return iousDao.countHallNewIous(start);
    }

    @Override
    public int countFollowUpdatedIous(int userId, Timestamp start) {
        return iousDao.countFollowUpdatedIous(userId, start);
    }

    @Override
    public int countBorrowOrLendUpdatedIous(Integer borrowerId, Integer lenderId, Timestamp start) {
        return iousDao.countBorrowOrLendUpdatedIous(borrowerId, lenderId, start);
    }

    @Override
    public IouIntention getIntentionByUserId(String iouCode, int userId) {
        return iousIntentionalUserDao.getIntentionByUserId(iouCode, userId);
    }

    @Override
    public List<Iou> getRecentIousForBorrower(int borrowerId) {
        List<Iou> ious = new ArrayList<Iou>();
        Timestamp currentDate = DateUtil.getCurrentTimeStamp();
        int[] statuses = Constants.IOU_STATUS_INEFFECT; // 排除没有生效的借条
        int size = 3;
        List<Iou> featureIous =
            iousDao.selectRecentIousByBorrowerId(borrowerId, currentDate, false, size, null, statuses);
        List<Iou> historyIous =
            iousDao.selectRecentIousByBorrowerId(borrowerId, currentDate, true, size, null, statuses);
        ious.addAll(historyIous);
        ious.addAll(featureIous);
        return ious;
    }

    @Override
    public int countIousByStatuses(int borrowerId, int[] statuses) {
        return iousDao.countIousByStatus(borrowerId, statuses);
    }

    @Override
    public IousAmountStatistic statisticIousAmountAndCnt(int[] statuses, Integer borrowerId, Integer lenderId,
        Timestamp start, Timestamp end) {
        return iousDao.statisticIousAmountAndCnt(statuses, borrowerId, lenderId, start, end);
    }

    @Override
    public List<Iou> selectIous(int[] statuses, int[] filterStatuses, String searchText, String iouCode,
        String userName, String phoneNumber, Integer lenderId, Integer borrowerId, String lastIouCode,
        Integer amountMin, Integer amountMax, Timestamp borrowingStart, Timestamp borrowingEnd,
        Timestamp repaymentStart, Timestamp repaymentEnd, Integer forward, int pageSize) {

        return iousDao.selectIous(statuses, filterStatuses, searchText, iouCode, userName, phoneNumber, lenderId,
            borrowerId, lastIouCode, amountMin, amountMax, borrowingStart, borrowingEnd, repaymentStart, repaymentEnd,
            forward, pageSize);
    }

    @Override
    public List<Iou> getIouFillList(Integer borrowerId, Integer lenderId, String lastIouCode, Integer forward,
        int pageSize) {
        int[] statuses = {20, 21, 30, 31};
        return selectIous(statuses, null, null, null, null, null, lenderId, borrowerId, lastIouCode, null, null, null,
            null, null, null, forward, pageSize);
    }

    @Override
    public List<Iou> selectIousForMessage(int[] statues, int pageSize, int messageType, Timestamp sendDate,
        Timestamp startDate, Timestamp endDate) {
        return iousDao.selectIousForMessage(statues, pageSize, messageType, sendDate, startDate, endDate);
    }

}
