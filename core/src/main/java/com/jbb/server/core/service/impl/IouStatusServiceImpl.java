package com.jbb.server.core.service.impl;

import java.sql.Timestamp;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.server.common.exception.AccessDeniedException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.dao.IousDao;
import com.jbb.server.core.domain.Iou;
import com.jbb.server.core.service.IouMessageProcessService;
import com.jbb.server.core.service.IouStatusService;
import com.jbb.server.core.service.IousService;
import com.jbb.server.shared.rs.Util;

@Service("IouStatusService")
public class IouStatusServiceImpl implements IouStatusService {

    @Autowired
    IousService iousService;

    @Autowired
    private IousDao iousDao;
    
    @Autowired
    private IouMessageProcessService iouMessageProcessService;

    @Override
    public void updateStatus(Iou iou, int newStatus, int userId, String extentionDateTs, Integer lenderId) {
        // 检查用户的操作权限
        int iouBorrowerId = iou.getBorrowerId();
        int iouLenderId = iou.getLenderId() != null ? iou.getLenderId() : 0;
        if (iouBorrowerId != userId && iouLenderId != userId) {
            // 非借款人或者出借人，不能修改借条状态
            throw new AccessDeniedException("jbb.error.exception.iouAccessDenied");
        }

        int preStatus = iou.getStatus();
        // 检查用户和状态权限
        if (iouBorrowerId == userId) {
            if (!iousDao.checkRightToUpdateStatus(preStatus, newStatus, false, true)) {
                throw new WrongParameterValueException("jbb.error.exception.wrongIouStatusUpdate", "zh",
                    iou.getStatus() + "->" + newStatus);
            }
        } else {
            if (!iousDao.checkRightToUpdateStatus(preStatus, newStatus, true, false)) {
                throw new WrongParameterValueException("jbb.error.exception.wrongIouStatusUpdate", "zh",
                    iou.getStatus() + "->" + newStatus);
            }
        }

        if (lenderId != null && iouBorrowerId == userId) {
            iou.setLenderId(lenderId);
        }

        //出借人和借款人都可以展期
        Timestamp eTs = Util.parseTimestamp(extentionDateTs, TimeZone.getDefault());
        if (lenderId != null && eTs!=null) {
            iou.setExtensionDate(eTs);
        }
        // 更新状态
        iou.setStatus(newStatus);
        iou.setLastUpdateStatusDate(DateUtil.getCurrentTimeStamp());

        if (newStatus == 6 || newStatus == 12) {
            // 确认延期，将还款日期设置成申请的时间,并更新库记录
            iou.setRepaymentDate(iou.getExtensionDate());
        }

        // 更新
        iousDao.updateIou(iou);
        
        //发送消息
        new Thread(() -> {
            iouMessageProcessService.sendIouMessage(preStatus, iou);
        }).start();

        // 修改申请， 更新意向人状态。 同意所选择的意向人，同时拒绝其他意向人
        if (newStatus == 2 && lenderId != null) {
            iousService.rejectIntentionalUsers(iou.getIouCode(), null, lenderId);
        }

    }

}
