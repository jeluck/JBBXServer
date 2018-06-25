package com.jbb.server.rs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbb.server.common.exception.ObjectNotFoundException;
import com.jbb.server.core.domain.Iou;
import com.jbb.server.core.service.IousService;

@Service(IouFollowAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class IouFollowAction extends BasicAction {

    private static Logger logger = LoggerFactory.getLogger(IouFollowAction.class);

    public static final String ACTION_NAME = "IouFollowAction";

    @Autowired
    private IousService iousService;

    public void updateStatus(String iouCode, int status) {
        logger.debug(">updateStatus, iouCode =" + iouCode + ", status=" + status);

        int userId = this.user.getUserId();
        Iou iou = iousService.getIouByIouCode(iouCode);
        if (iou == null) {
            throw new ObjectNotFoundException("jbb.error.exception.iouNotFournd", "zh", iouCode);
        }
        iousService.updateIouFollowStatus(userId, iouCode, status);

        logger.debug("<updateStatus");
    }
}