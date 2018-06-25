package com.jbb.server.rs.action;

import java.sql.Timestamp;
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
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.DuplicateEntityException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.domain.LenderCounts;
import com.jbb.server.core.domain.LenderReason;
import com.jbb.server.core.domain.SourceCounts;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.domain.UserProperty;
import com.jbb.server.core.service.UserEventLogService;
import com.jbb.server.core.service.UserService;
import com.jbb.server.core.util.PasswordUtil;
import com.jbb.server.rs.pojo.ActionResponse;
import com.jbb.server.rs.pojo.request.ReChannel;

@Service(ChannelAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ChannelAction extends BasicAction {

    private static Logger logger = LoggerFactory.getLogger(ChannelAction.class);

    public static final String ACTION_NAME = "ChannelAction";

    public static final int CHANNEL_SOURCE_ROLE_ID = 3;

    public static final int CHANNEL_LENDER_ROLE_ID = 2;

    private Response response;

    @Autowired
    private UserEventLogService userEventLogService;

    @Autowired
    private UserService userService;

    @Override
    protected ActionResponse makeActionResponse() {
        return response = new Response();
    }

    public void getChannels(Integer roleId) {
        this.response.user = this.coreAccountService.getUsersByRole(roleId);
        int days = PropertyManager.getIntProperty("jbb.channel.statistic.days", 3);
        if (roleId != null && roleId == CHANNEL_SOURCE_ROLE_ID) {
            long ts = DateUtil.getTodayStartCurrentTime() - days * DateUtil.DAY_MILLSECONDES;
            this.response.sourceCounts = userService.countUsersForSource(new Timestamp(ts), null);
        } else if (roleId != null && roleId == CHANNEL_LENDER_ROLE_ID) {
            long ts = DateUtil.getTodayStartCurrentTime() - days * DateUtil.DAY_MILLSECONDES;
            this.response.lenderCounts = userService.countUsersForLender(new Timestamp(ts), null);
        }

    }

    public void registerChannel(String phoneNumber, String username, String password, Integer roleId, String sourceIds,
        ReChannel req) {
        if (logger.isDebugEnabled()) {
            logger.debug(">registerChannel()" + ", phoneNumber=" + phoneNumber + ",  username=" + username
                + ",  sourceIds=" + sourceIds);
        }
        // 不能创建超级管理员
        if (roleId == null || roleId == 0) {
            throw new WrongParameterValueException("jbb.error.exception.roleIdError");
        }
        // 添加检查代码
        if (StringUtil.isEmpty(phoneNumber)) {
            throw new WrongParameterValueException("jbb.error.exception.phoneNumberEmpty");
        }
        if (!PasswordUtil.isValidUserPassword(password)) {
            throw new WrongParameterValueException("jbb.error.exception.passwordCheckError");
        }

        // 注册用户
        User registerUser = null;
        try {
            registerUser =
                coreAccountService.createUserFormAdmin(this.user.getUserId(), phoneNumber, username, password, roleId);
            if (registerUser.getUserId() != 0) {
                // 插入注册日志
                int userId = registerUser.getUserId();
                String remoteAddress = this.getRemoteAddress();
                userEventLogService.insertEventLog(userId, "Admin", Constants.Event_LOG_EVENT_USER_EVENT,
                    Constants.Event_LOG_EVENT_USER_ACTION_REGISTER, null, null, remoteAddress);
            }
        } catch (DuplicateEntityException e) {
            throw new DuplicateEntityException("jbb.error.exception.duplicateChannel");
        }
        if (!StringUtil.isEmpty(sourceIds)) {
            // 如果是渠道方，更新渠道属性字段
            coreAccountService.saveUserProperty(registerUser.getUserId(), Constants.CHANNEL_SOURCE_ID, sourceIds);
        }
        if (req != null) {
            List<UserProperty> userProperties = new ArrayList<UserProperty>();
            userProperties.add(new UserProperty(UserProperty.P_CHANNEL_FILTER_EXPRESSION, req.getFilterExpression()));
            userProperties.add(new UserProperty(UserProperty.P_CHANNEL_SOURCES, req.getSources()));
            userProperties.add(new UserProperty(UserProperty.P_CHANNEL_WEIGHT, String.valueOf(req.getWeight())));
            userProperties.add(new UserProperty(UserProperty.P_CHANNEL_MAX_CNT, String.valueOf(req.getMaxCnt())));
            // 更新过滤表达式
            coreAccountService.updateUserProperties(registerUser.getUserId(), userProperties);
        }
        logger.debug("<registerChannel");
    }

    public void updateChannelUser(String phoneNumber, String username, String password, Integer roleId,
        String sourceIds, ReChannel req) {
        if (logger.isDebugEnabled()) {
            logger.debug(">updateChannelUser()" + ", phoneNumber=" + phoneNumber + ",  username=" + username
                + ",  sourceIds=" + sourceIds);
        }

        // 不能创建超级管理员
        if (roleId == null || roleId == 0) {
            throw new WrongParameterValueException("jbb.error.exception.roleIdError");
        }

        // 添加检查代码
        if (StringUtil.isEmpty(phoneNumber)) {
            throw new WrongParameterValueException("jbb.error.exception.phoneNumberEmpty");
        }

        // 注册用户
        User registerUser = null;
        try {
            registerUser = this.coreAccountService.getUserByPhoneNumber(phoneNumber);
            boolean changed = false;
            if (!StringUtil.isEmpty(username)) {
                changed = true;
                registerUser.setUserName(username);
            }

            if (!StringUtil.isEmpty(password)) {
                if (!PasswordUtil.isValidUserPassword(password)) {
                    throw new WrongParameterValueException("jbb.error.exception.passwordCheckError");
                }
                changed = true;
                registerUser.setPassword(PasswordUtil.passwordHash(password));
            }

            if (roleId != null) {
                changed = true;
                registerUser.setRoleId(roleId);
            }

            if (changed) {
                this.coreAccountService.updateUser(registerUser);
            }
        } catch (DuplicateEntityException e) {

        }
        if (!StringUtil.isEmpty(sourceIds)) {
            // 如果是渠道方，更新渠道属性字段
            coreAccountService.saveUserProperty(registerUser.getUserId(), Constants.CHANNEL_SOURCE_ID, sourceIds);
        }
        if (req != null) {

            List<UserProperty> userProperties = new ArrayList<UserProperty>();
            userProperties.add(new UserProperty(UserProperty.P_CHANNEL_FILTER_EXPRESSION, req.getFilterExpression()));
            userProperties.add(new UserProperty(UserProperty.P_CHANNEL_SOURCES, req.getSources()));
            userProperties.add(new UserProperty(UserProperty.P_CHANNEL_WEIGHT, String.valueOf(req.getWeight())));
            userProperties.add(new UserProperty(UserProperty.P_CHANNEL_MAX_CNT, String.valueOf(req.getMaxCnt())));
            // 更新过滤表达式
            coreAccountService.updateUserProperties(registerUser.getUserId(), userProperties);
        }
        logger.debug("<updateChannelUser");
    }

    public void updateReason(String phoneNumber, LenderReason reason) {
        if (logger.isDebugEnabled()) {
            logger.debug(">updateReason()" + ", phoneNumber=" + phoneNumber + ",  reason=" + reason);
        }
        User applayUser = coreAccountService.getUserByPhoneNumber(phoneNumber);
        if (applayUser != null) {
            coreAccountService.updateTargetUserReason(applayUser.getUserId(), this.user.getUserId(),reason);
        }
        logger.debug("<updateReason");

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        // 渠道用户列表
        private List<User> user;

        // 出借人统计数据
        private List<LenderCounts> lenderCounts;

        // 来源方统计数据
        private List<SourceCounts> sourceCounts;

        public List<User> getUser() {
            return user;
        }

        public List<LenderCounts> getLenderCounts() {
            return lenderCounts;
        }

        public List<SourceCounts> getSourceCounts() {
            return sourceCounts;
        }

    }

}
