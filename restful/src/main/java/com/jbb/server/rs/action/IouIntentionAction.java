package com.jbb.server.rs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbb.server.common.Constants;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.exception.ObjectNotFoundException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.core.domain.Iou;
import com.jbb.server.core.service.IousService;

@Service(IouIntentionAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class IouIntentionAction extends BasicAction {

    private static Logger logger = LoggerFactory.getLogger(IouIntentionAction.class);

    public static final String ACTION_NAME = "IouIntentionAction";

    @Autowired
    private IousService iousService;

    public void updateIntentionStatus(String iouCode, int status, Integer intendedUserId) {
        logger.debug(">updateIntentionStatus, iouCode =" + iouCode + ", status=" + status + ", intendedUserId="
            + intendedUserId);

        int userId = this.user.getUserId();

        Iou iou = iousService.getIouByIouCode(iouCode);

        if (iou == null) {
            throw new ObjectNotFoundException("jbb.error.exception.iouNotFournd", "zh", iouCode);
        }
        // 借款人更新状态
        if (userId == iou.getBorrowerId()) {

            if (status != Constants.IOU_INTEND_REJECT && status != Constants.IOU_INTEND_APPROVE) {
                // 出借人只能拒绝意向人
                throw new WrongParameterValueException("jbb.error.exception.canotLendToSelf", "zh");
            }

            if (intendedUserId == null && status == Constants.IOU_INTEND_REJECT) {
                // 拒绝所有人
                iousService.rejectIntentionalUsers(iouCode, null, null);
                return;
            }

            if (intendedUserId == null) {
                // 意向人ID不存在
                throw new MissingParameterException("jbb.error.exception.missing.param", "zh", "intendedUserId");
            }

            iousService.updateIouIntentionalStatus(intendedUserId, iouCode, status);
        } else {
            // 出借人更新状态
            if (status != Constants.IOU_INTEND_YES && status != Constants.IOU_INTEND_NO && status != Constants.IOU_INTEND_MODIFY_APPROVE 
                && status != Constants.IOU_INTEND_MODIFY_REJECT) {
                // 出借人只能拒绝意向人
                throw new WrongParameterValueException("jbb.error.exception.iouStatusError", "zh");
            }
            iousService.updateIouIntentionalStatus(userId, iouCode, status);
        }
        logger.debug("<updateIntentionStatus");
    }
}