
package com.jbb.mgt.rs.action.user;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.UserApplyRecord;
import com.jbb.mgt.core.service.AccountService;
import com.jbb.mgt.core.service.UserApplyRecordService;
import com.jbb.mgt.core.service.UserService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.server.common.exception.ObjectNotFoundException;
import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;

@Service(UserApplyAssignAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserApplyAssignAction extends BasicAction {

    public static final String ACTION_NAME = "UserApplyAssignAction";

    private static Logger logger = LoggerFactory.getLogger(UserApplyAssignAction.class);

    @Autowired
    @Qualifier("AccountService")
    private AccountService accountService;
    @Autowired
    @Qualifier("UserService")
    private UserService userService;
    @Autowired
    @Qualifier("UserApplyRecordService")
    private UserApplyRecordService userApplyRecordService;

    /**
     * 分配用户
     * 
     * @param request
     */
    public void assignApplyUsers(Request req) {

        // {
        // "num": 10, //如果有值，且isApplyAllUsers 为false, 先把num个用户分配
        // "accountIds":[1,2], //初审员列表
        // "applyIds":[1,2,3], //按applyId分配
        // "isApplyAllUsers":true //如果为true,分配库里未分配的所有用户。优先级最高
        // }
        logger.debug(">assignApplyUsers(), isAllApplyUsers= " + req.isApplyAllUsers + "num= " + req.num + "accountIds= "
            + StringUtil.concatenate(req.accountIds, ',') + "applyIds= " + StringUtil.concatenate(req.applyIds, ','));

        // TODO 添加检验字段逻辑

        if (req.accountIds == null || req.accountIds.length == 0) {
            return;
        }

        List<UserApplyRecord> applyRecords = null;
        // 按需获取数据
        if (req.isApplyAllUsers != null && req.isApplyAllUsers == true) {
            // 获取所有未分配的数据
            applyRecords = userApplyRecordService.getUnassignUserApplyRecords(null);
        } else if (req.num != null && req.num > 0) {
            // 获取num条数据
            applyRecords = userApplyRecordService.getUnassignUserApplyRecords(req.num);
        }

        // 更新数据，将applyUser分配给相应的accoudId
        int accSize = req.accountIds.length;
        Map<Integer, Set<Integer>> accMap = new HashMap<Integer, Set<Integer>>();

        for (int i = 0; i < accSize; i++) {
            accMap.put(i, new HashSet<Integer>());
        }
        int index = 0;
        if (!CommonUtil.isNullOrEmpty(applyRecords)) {

            for (UserApplyRecord u : applyRecords) {
                accMap.get(index % accSize).add(u.getApplyId());
                index++;
            }
        }

        if (!CommonUtil.isNullOrEmpty(req.applyIds)) {
            for (Integer applyId : req.applyIds) {
                accMap.get(index % accSize).add(applyId);
                index++;
            }
        }

        Timestamp ts = DateUtil.getCurrentTimeStamp();
        accMap.forEach((key, applyUserRecordSet) -> {
            if (applyUserRecordSet == null || applyUserRecordSet.size() == 0) {
                return;
            }
            userApplyRecordService.updateUserAppayRecordAssignAccIdInBatch(
                CommonUtil.toIntArray(applyUserRecordSet, -1), this.account.getAccountId(), ts, req.accountIds[key]);
        });

        logger.debug("<assignUser()");
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Request {
        public int[] applyIds;
        public int[] accountIds;
        public Boolean isApplyAllUsers;
        public Integer num;
    }

}
