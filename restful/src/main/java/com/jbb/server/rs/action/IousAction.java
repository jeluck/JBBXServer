package com.jbb.server.rs.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbb.server.common.Constants;
import com.jbb.server.common.exception.AccessDeniedException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.core.domain.Iou;
import com.jbb.server.core.service.IousService;
import com.jbb.server.rs.pojo.ActionResponse;
import com.jbb.server.shared.rs.Util;

@Service(IousAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class IousAction extends BasicAction {

    private static Logger logger = LoggerFactory.getLogger(IousAction.class);

    public static final String ACTION_NAME = "IousAction";

    public static final int MAX_SIZE = 20;

    private Response response;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    @Autowired
    private IousService iousService;

    /**
     * 借条中心
     * 
     * @param userId
     * @param type
     */
    public void getIous(Integer category, Integer forward, String lastIouCode, Integer size, int[] statuses,
        int[] filterStatuses,
        String iouCode, String userName, String phoneNumber, Integer amountMin, Integer amountMax,
        String borrowingStart, String borrowingEnd, String repaymentStart, String repaymentEnd, 
        String searchText) {
        if (size == null || size > MAX_SIZE || size <= 0) {
            size = MAX_SIZE;
        }
        
        // 查询大厅里用户发布的借条
        if (category == Constants.IOU_CATEGORY_HALL) {
            this.response.iousList = iousService.getIousForHall(lastIouCode, forward, size);
            this.response.iousList.forEach(iou -> iou.prepareCalFields());
            return;
        }
        // 其他
        if (this.user == null) {
            throw new AccessDeniedException("jbb.error.exception.accessDenied.userEmpty", "zh");
        }
        int userId = this.user.getUserId();
        TimeZone tz = TimeZone.getDefault();
        Timestamp bStart = Util.parseTimestamp(borrowingStart,tz);
        Timestamp bEnd = Util.parseTimestamp(borrowingEnd, tz);
        Timestamp pStart = Util.parseTimestamp(repaymentStart, tz);
        Timestamp pEnd = Util.parseTimestamp(repaymentEnd, tz);
        switch (category) {
            case Constants.IOU_CATEGORY_FOLLOW:
                this.response.iousList = iousService.getFollowedIousByUserId(userId);
                break;
            case Constants.IOU_CATEGORY_LEND:
                this.response.iousList = iousService.getLendIousByLenderId(userId);
                break;
            case Constants.IOU_CATEGORY_PUBLISY:
                List<Iou> ious = iousService.getPublishedIousByBorrowerId(userId);
                ious.forEach(iou -> {
                    iou.setIntendUserCnt(iousService.countIntentionalUsers(iou.getIouCode()));
                });
                this.response.iousList = ious;
                break;
            case Constants.IOU_CATEGORY_BORROWER_FILL:
                // 借款人补借条列表
                this.response.iousList = iousService.getIouFillList(userId, null, lastIouCode, forward, size);
                break;
            case Constants.IOU_CATEGORY_LENDER_FILL:
                // 出借人补借条列表
                this.response.iousList = iousService.getIouFillList(null, userId, lastIouCode, forward, size);
                break;
            case Constants.IOU_CATEGORY_IOUS_LENDER:
                // 出借人借出的借条
                if (statuses == null || statuses.length == 0) {
                    statuses = Constants.IOU_STATUS_EFFECT;
                } else {
                    // checkStatus
                    for (int status : statuses) {
                        if (!CommonUtil.inArray(status, Constants.IOU_STATUS_EFFECT)) {
                            throw new WrongParameterValueException("jbb.error.exception.iouStatusError");
                        }
                    }
                }

                this.response.iousList = iousService.selectIous(statuses, filterStatuses, searchText, iouCode, userName, phoneNumber, userId, null,
                    lastIouCode, amountMin, amountMax, bStart, bEnd, pStart, pEnd, forward, size);
                break;
            case Constants.IOU_CATEGORY_IOUS_BORROWER:
                // 借款人借入的借条
                if (statuses == null || statuses.length == 0) {
                    statuses = Constants.IOU_STATUS_EFFECT;
                } else {
                    // checkStatus
                    for (int status : statuses) {
                        if (!CommonUtil.inArray(status, Constants.IOU_STATUS_EFFECT)) {
                            throw new WrongParameterValueException("jbb.error.exception.iouStatusError");
                        }
                    }
                }
                this.response.iousList = iousService.selectIous(statuses, filterStatuses, searchText, iouCode, userName, phoneNumber, null, userId,
                    lastIouCode, amountMin, amountMax, bStart, bEnd, pStart, pEnd, forward, size);
                break;
            case Constants.IOU_CATEGORY_IOUS_PUBLISH:
                // 借款人发布的借条
                int[] statusArr = {0, 2};
                this.response.iousList = iousService.selectIous(statusArr, filterStatuses, searchText, iouCode, userName, phoneNumber, null, userId,
                    lastIouCode, amountMin, amountMax, bStart, bEnd, pStart, pEnd, forward, size);
                break;
        }
        // 计算其他需要计算的字段
        if(this.response.iousList!=null){
            this.response.iousList.forEach(iou -> iou.prepareCalFields());
        } 
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {

        // 借条列表
        private List<Iou> iousList;

        @JsonProperty("ious")
        public List<Iou> getIousList() {
            return iousList;
        }

    }
}
