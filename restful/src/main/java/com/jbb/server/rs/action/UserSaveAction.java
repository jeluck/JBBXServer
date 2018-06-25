package com.jbb.server.rs.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbb.server.common.Constants;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.DuplicateEntityException;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.IDCardUtil;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.core.domain.User;
import com.jbb.server.core.service.PaService;
import com.jbb.server.core.service.UserEventLogService;
import com.jbb.server.core.service.UserService;
import com.jbb.server.core.util.PasswordUtil;
import com.jbb.server.core.util.ProcessUserOnStepOneUtil;
import com.jbb.server.mq.MqClient;
import com.jbb.server.mq.Queues;
import com.jbb.server.rs.pojo.request.ReUser;

@Service(UserSaveAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserSaveAction extends BasicAction {

    private static Logger logger = LoggerFactory.getLogger(UserSaveAction.class);

    public static final String ACTION_NAME = "UserSaveAction";

    @Autowired
    private UserEventLogService userEventLogService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaService paService;

    public void registerUser(String phoneNumber, String msgCode, String nickname, String password, String platform,
        String sourceId) {
        if (logger.isDebugEnabled()) {
            logger.debug(">registerUser()" + ", phoneNumber=" + phoneNumber + ",  msgCode=" + msgCode + ", sourceId="
                + sourceId + ", nickName=" + nickname + ", platform=" + platform);
        }

        // 检查nickname唯一性
        if (!StringUtil.isEmpty(nickname) && coreAccountService.checkNickname(nickname)) {
            throw new WrongParameterValueException("jbb.error.exception.nicknameDuplicate");
        }

        // 添加检查代码
        if (StringUtil.isEmpty(phoneNumber)) {
            throw new WrongParameterValueException("jbb.error.exception.phoneNumberEmpty");
        }
        if (!PasswordUtil.isValidUserPassword(password)) {
            throw new WrongParameterValueException("jbb.error.exception.passwordCheckError");
        }

        // 注册用户
        User user = coreAccountService.createUser(phoneNumber, nickname, password, msgCode, sourceId, platform);
        if (user.getUserId() != 0) {
            // 插入注册日志
            int userId = user.getUserId();
            String remoteAddress = this.getRemoteAddress();
            userEventLogService.insertEventLog(userId, sourceId, Constants.Event_LOG_EVENT_USER_EVENT,
                Constants.Event_LOG_EVENT_USER_ACTION_REGISTER, null, null, remoteAddress);
        }
        logger.debug("<registerUser");
    }

    private User convertToUser(ReUser reUser) {

        User user = new User();
        user.setPhoneNumber(reUser.getPhoneNumber());
        user.setUserName(reUser.getUserName());
        user.setIdCardNo(reUser.getIdCardNo());
        user.setWechat(reUser.getWechat());
        user.setProperites(reUser.getProperites());
        return user;
    }

    // 分步检查用户提交的数据
    private void checkH5UserInfo(ReUser reUser, Integer step, String sourceId, Integer validateSesameCreditScoreStep) {

        if (validateSesameCreditScoreStep == null) {
            validateSesameCreditScoreStep = 1;
        }

        if (step == null || step == 1) {

            if (StringUtil.isEmpty(sourceId)) {
                throw new MissingParameterException("jbb.error.exception.missing.param", "zh", "sourceId");
            }

            if (StringUtil.isEmpty(reUser.getPhoneNumber())) {
                throw new MissingParameterException("jbb.error.exception.missing.param", "zh", "phoneNumber");
            }
            
            //第一步验证
            if ((step == null || validateSesameCreditScoreStep == null || validateSesameCreditScoreStep == 1)
                && !userService.checkExistPropertyAndNotNull(reUser.getProperites(), Constants.SESAME_CREDIT_SCORE)) {
                throw new MissingParameterException("jbb.error.exception.missing.param", "zh",
                    "property->sesameCreditScore");
            }

        }

        if (step == null || step == 2) {
            if (StringUtil.isEmpty(reUser.getUserName())) {
                throw new MissingParameterException("jbb.error.exception.missing.param", "zh", "username");
            }

            if (StringUtil.isEmpty(reUser.getIdCardNo())) {
                throw new MissingParameterException("jbb.error.exception.missing.param", "zh", "idcardno");
            }

            if (StringUtil.isEmpty(reUser.getWechat())) {
                throw new MissingParameterException("jbb.error.exception.missing.param", "zh", "wechat");
            }
            
            //第二步验证
            if ((step != null && step == 2 && validateSesameCreditScoreStep == 2)
                && !userService.checkExistPropertyAndNotNull(reUser.getProperites(), Constants.SESAME_CREDIT_SCORE)) {
                throw new MissingParameterException("jbb.error.exception.missing.param", "zh",
                    "property->sesameCreditScore");
            }

            // 检查身份证号
            String idcard = reUser.getIdCardNo();
            if (!IDCardUtil.validate(idcard) || !userService.chekcIdCardRegion(idcard)) {
                throw new WrongParameterValueException("jbb.error.exception.idCardError", "zh");
            }
        }
        
        
    }

    public void registerUserFromH5(ReUser reUser, String sourceId, String msgCode, String platform,
        List<Integer> targetUserIds, Integer step, Boolean postToPA, Integer validateSesameCreditScoreStep) {

        logger.debug(">registerUserFromH5() , reUser" + reUser + "scourceId=" + sourceId + "platform=" + platform);
        if (StringUtil.isEmpty(msgCode)) {
            throw new MissingParameterException("jbb.error.exception.missing.param", "zh", "msgCode");
        }

        // 检查数据
        checkH5UserInfo(reUser, step, sourceId, validateSesameCreditScoreStep);

        try {
            // 注册用户
            User user = convertToUser(reUser);
            user.setSourceId(sourceId);
            user.setPlatform(platform);
            coreAccountService.createUser(user, msgCode);
            if (user.getUserId() != 0) {
                // 插入注册日志
                int userId = user.getUserId();
                String remoteAddress = this.getRemoteAddress();
                userEventLogService.insertEventLog(userId, sourceId, Constants.Event_LOG_EVENT_USER_EVENT,
                    Constants.Event_LOG_EVENT_USER_ACTION_REGISTER, null, null, remoteAddress);
            }
            this.user = user;
            // 注册第一步：延时发送至MQ， 延时x秒
            int delay = PropertyManager.getIntProperty("jbb.user.register.step1.delay", 300);
            ProcessUserOnStepOneUtil.sendUserWithDelay(user.getUserId(), delay);

        } catch (DuplicateEntityException e) {
            // 获取并更新用户信息
            this.user = this.coreAccountService.loginByCode(reUser.getPhoneNumber(), msgCode, platform);
            if (this.user.getIdCardNo() != null && !this.user.getIdCardNo().equals(reUser.getIdCardNo())) {
                throw new WrongParameterValueException("jbb.error.exception.idcardnoNotEqual");
            }
            if (this.user.getUserName() != null && !this.user.getUserName().equals(reUser.getUserName())) {
                throw new WrongParameterValueException("jbb.error.exception.usernameNotEqual");
            }
            updateUser(null, reUser);

            // 注册第二步， 更新用户信息时，发送用户ID到消息队列，进行下一步处理。
            // 发送消息队列，进行注册广播 , 后续处理分配 到相应的申请机构
            int registerUserId = this.user.getUserId();
            // 移除第一步的延时发送至MQ
            ProcessUserOnStepOneUtil.removeSendTask(registerUserId);
            // 立即发送至MQ
            MqClient.send(Queues.USER_REGISTER_QUEUE_ADDR, String.valueOf(registerUserId).getBytes(), 0);

            int userId = user.getUserId();
            String remoteAddress = this.getRemoteAddress();
            if (userId != 0) {
                // 插入更新日志
                userEventLogService.insertEventLog(userId, sourceId, Constants.Event_LOG_EVENT_USER_EVENT,
                    Constants.Event_LOG_EVENT_USER_ACTION_UPDATE, null, null, remoteAddress);
            }

            // 将数据推送给平安
            if (postToPA != null && true == postToPA) {
                paService.postUserToPa(user, remoteAddress);
            }

        }

        logger.debug("<registerUserFromH5()");
    }

    public void updateUser(String password, ReUser reUser) {
        logger.debug(">updateUser(), reUser = {}", reUser);

        if (StringUtil.trimToNull(password) != null) {
            if (!PasswordUtil.isValidUserPassword(password)) {
                throw new WrongParameterValueException("jbb.error.exception.passwordCheckError");
            }
            // 更新密码
            coreAccountService.updatePassword(user.getUserId(), password);
        }
        // 将请求的reUser中的更新数据添加到原来的user对象
        boolean changed = convertReUser2User(user, reUser);
        // 更新用户数据到数据库
        if (changed) {
            // 检查nickname唯一性
            String nickname = user.getNickName();
            if (!StringUtil.isEmpty(StringUtil.trimToNull(reUser.getNickName()))) {
                if (!StringUtil.isEmpty(nickname) && coreAccountService.checkNickname(nickname)) {
                    throw new WrongParameterValueException("jbb.error.exception.nicknameDuplicate");
                }
            }
            coreAccountService.updateUser(user);
        }

        logger.debug("<updateUser()");
    }

    // TODO
    private boolean convertReUser2User(User user, ReUser reUser) {
        if (reUser == null) {
            return false;
        }
        boolean changed = false;
        String value;

        if ((value = StringUtil.trimToNull(reUser.getBankCardNo())) != null) {
            user.setBankCardNo(value);
            changed = true;
        }
        if ((value = StringUtil.trimToNull(reUser.getBankName())) != null) {
            user.setBankName(value);
            changed = true;
        }
        if ((value = StringUtil.trimToNull(reUser.getEmail())) != null) {
            user.setEmail(value);
            changed = true;
        }
        if ((value = StringUtil.trimToNull(reUser.getIdCardNo())) != null) {
            user.setIdCardNo(value);
            changed = true;
        }
        if ((value = StringUtil.trimToNull(reUser.getNickName())) != null) {
            user.setNickName(value);
            changed = true;
        }
        if ((value = StringUtil.trimToNull(reUser.getUserName())) != null) {
            user.setUserName(value);
            changed = true;
        }

        if ((value = StringUtil.trimToNull(reUser.getSex())) != null) {
            user.setSex(value);
            changed = true;
        }
        if (reUser.getAge() != null) {
            user.setAge(reUser.getAge());
            changed = true;
        }
        if ((value = StringUtil.trimToNull(reUser.getNation())) != null) {
            user.setNation(value);
            changed = true;
        }
        if ((value = StringUtil.trimToNull(reUser.getIdcardAddress())) != null) {
            user.setIdcardAddress(value);
            changed = true;
        }
        if ((value = StringUtil.trimToNull(reUser.getWechat())) != null) {
            user.setWechat(value);
            changed = true;
        }
        if ((value = StringUtil.trimToNull(reUser.getQqNumber())) != null) {
            user.setQqNumber(value);
            changed = true;
        }
        if (reUser.getAddressBookNumber() != null) {
            user.setAddressBookNumber(reUser.getAddressBookNumber());
            changed = true;
        }
        if ((value = StringUtil.trimToNull(reUser.getPhoneAuthenticationTime())) != null) {
            user.setPhoneAuthenticationTime(value);
            changed = true;
        }
        if (reUser.getMarried() != null) {
            user.setMarried(reUser.getMarried());
            changed = true;
        }
        if ((value = StringUtil.trimToNull(reUser.getLiveAddress())) != null) {
            user.setLiveAddress(value);
            changed = true;
        }
        if ((value = StringUtil.trimToNull(reUser.getParentAddress())) != null) {
            user.setParentAddress(value);
            changed = true;
        }
        if ((value = StringUtil.trimToNull(reUser.getEducation())) != null) {
            user.setEducation(value);
            changed = true;
        }
        if ((value = StringUtil.trimToNull(reUser.getOccupation())) != null) {
            user.setOccupation(value);
            changed = true;
        }
        if ((value = StringUtil.trimToNull(reUser.getWechat())) != null) {
            user.setWechat(value);
            changed = true;
        }
        if (reUser.getProperites() != null && reUser.getProperites().size() > 0) {
            user.setProperites(reUser.getProperites());
            changed = true;
        }
        return changed;

    }

}
