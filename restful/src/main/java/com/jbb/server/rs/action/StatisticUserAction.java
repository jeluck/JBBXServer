package com.jbb.server.rs.action;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.server.common.Constants;
import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.common.util.IDCardUtil;
import com.jbb.server.core.domain.Region;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserCounts;
import com.jbb.server.core.domain.UserProperty;
import com.jbb.server.core.service.UserService;
import com.jbb.server.rs.pojo.ActionResponse;
import com.jbb.server.rs.pojo.RsRecommandUser;

@Service(StatisticUserAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StatisticUserAction extends BasicAction {
    private static Logger logger = LoggerFactory.getLogger(StatisticUserAction.class);
    public static final String ACTION_NAME = "StatisticUserAction";

    private Response response;

    @Autowired
    UserService userService;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    public void statisticUserBySourceId(String dateStr, Integer userId) {
        logger.debug(">statisticUserBySourceId()");

        User sourceUser = null;
        if (userId != null) {
            sourceUser = this.coreAccountService.getUser(userId);
        } else {
            sourceUser = this.user;
            userId = this.user.getUserId();
        }

        UserProperty p =
            this.coreAccountService.searchUserPropertiesByUserIdAndName(userId, Constants.CHANNEL_SOURCE_ID);
        if (p != null) {
            String sourceIdsStr = p.getValue();
            String[] sourceIds = sourceIdsStr.split(",");
            this.response.userCnts = userService.statisticUser(sourceIds, dateStr);
            List<User> users = userService.getRecommandUsersBySourceIds(sourceIds, dateStr);
            List<RsRecommandUser> rUsers = new ArrayList<RsRecommandUser>(users.size());
            for (User u : users) {
                String idcard = u.getIdCardNo();
                if (IDCardUtil.validate(idcard)) {
                    Region region = userService.getRegionByIdCard(idcard);
                    if (region != null) {
                        u.setBirthPlace(region.getArea());
                        u.setAge(IDCardUtil.calculateAge(idcard));
                        u.setSex(IDCardUtil.calculateSex(idcard));
                    }
                }
                if (this.user.checkAccessAllPermission()) {
                    // 如果是管理员，返回数据
                    RsRecommandUser rsUser = new RsRecommandUser(u, false);
                    // 添加申请用户来源列表
                    rsUser.setTargetUsers(getTargetUserIds(u.getUserId()));
                    rUsers.add(rsUser);

                } else {
                    // 如果是渠道方，返回脱敏数据
                    rUsers.add(new RsRecommandUser(u, true));
                }
            }
            this.response.users = rUsers;
        }

        this.response.channelName = sourceUser.getUserName();

        logger.debug("<statisticUserBySourceId()");
    }

    private String getTargetUserIds(int userId) {
        List<Integer> targetUserIds = userService.getTargetUserIds(userId);
        String retStr = "";
        if (!CommonUtil.isNullOrEmpty(targetUserIds)) {
            for (Integer targetUserId : targetUserIds) {
                retStr += targetUserId +"|";
            }
        }
        return retStr;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private List<UserCounts> userCnts;
        private String channelName;
        List<RsRecommandUser> users;

        public String getChannelName() {
            return channelName;
        }

        public List<UserCounts> getUserCnts() {
            return userCnts;
        }

        public List<RsRecommandUser> getUsers() {
            return users;
        }

    }

}
