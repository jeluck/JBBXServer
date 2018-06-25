package com.jbb.mgt.rs.action.user;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.dao.MessageCodeDao;
import com.jbb.mgt.core.domain.Account;
import com.jbb.mgt.core.domain.Channel;
import com.jbb.mgt.core.domain.IPAddressInfo;
import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.core.domain.UserApplyRecord;
import com.jbb.mgt.core.domain.UserKey;
import com.jbb.mgt.core.domain.UserResponse;
import com.jbb.mgt.core.domain.mapper.Mapper;
import com.jbb.mgt.core.domain.mq.RegisterEvent;
import com.jbb.mgt.core.service.AliyunService;
import com.jbb.mgt.core.service.ChannelService;
import com.jbb.mgt.core.service.JbbService;
import com.jbb.mgt.core.service.UserApplyRecordService;
import com.jbb.mgt.core.service.UserService;
import com.jbb.mgt.rs.action.integrate.SyncJbbUserIdThread;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.mgt.server.rs.pojo.RsLoginLog;
import com.jbb.server.common.Constants;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.mq.MqClient;
import com.jbb.server.mq.Queues;

@Service(LoginUserAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LoginUserAction extends BasicAction {

    public static final String ACTION_NAME = "LoginUserAction";

    private static Logger logger = LoggerFactory.getLogger(LoginUserAction.class);

    private static DefaultTransactionDefinition NEW_TX_DEFINITION =
        new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

    private Response response;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    @Autowired
    @Qualifier("UserService")
    private UserService userService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private AliyunService aliyunService;

    @Autowired
    @Qualifier("MessageCodeDao")
    private MessageCodeDao messageCodeDao;

    @Autowired
    private UserApplyRecordService userApplyRecordService;

    @Autowired
    private PlatformTransactionManager txManager;

    private void rollbackTransaction(TransactionStatus txStatus) {
        if (txStatus == null) {
            return;
        }

        try {
            txManager.rollback(txStatus);
        } catch (Exception er) {
            logger.warn("Cannot rollback transaction", er);
        }
    }

    public void loginUserByPhoneNumber(String phoneNumber, String msgCode, String channelCode, Integer initAccountId,
        String platform, String mobileManufacture, Boolean test) {
        logger.debug(">createUser() phoneNumber={}", phoneNumber);
        if (StringUtil.isEmpty(phoneNumber)) {
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "phoneNumber");
        }
        if (StringUtil.isEmpty(msgCode)) {
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "msgCode");
        }
        if (StringUtil.isEmpty(channelCode)) {
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "channelCode");
        }
        TransactionStatus txStatus = null;
        boolean isMsgCoede =
            messageCodeDao.checkMsgCode(phoneNumber, channelCode, msgCode, DateUtil.getCurrentTimeStamp());
        if (isMsgCoede) {
            // TODO 后续优化查询逻辑
            Channel channel = channelService.getChannelByCode(channelCode);
            Account creator = this.coreAccountService.getAccountById(channel.getCreator(), null, false);
            User user = this.userService.selectUser(creator.getOrgId(), phoneNumber);
            if (user == null) {
                user = new User();
                user.setChannelCode(channelCode);
                user.setPhoneNumber(phoneNumber);
                user.setCreationDate(DateUtil.getCurrentTimeStamp());
                user.setIpAddress(this.getRemoteAddress());
                user.setPlatform(platform);
                user.setMobileManufacture(mobileManufacture);
                IPAddressInfo ipAddressInfo = aliyunService.getIPAddressInfo(this.getRemoteAddress());
                if (ipAddressInfo != null) {
                    user.setIpArea(ipAddressInfo.getIpArea());
                }

                try {
                    txStatus = txManager.getTransaction(NEW_TX_DEFINITION);
                    // 插入用户
                    this.userService.insertUser(user);
                    // 检查这个用户进来的渠道是属于哪个组织 。 channel -> orgId, 再把这个这个用户插入到mgt_user_apply_records
                    this.insertApplyRecord(user, creator, initAccountId);

                    // 更新jbbId
                    new SyncJbbUserIdThread(user).start();

                    // 如查渠道是JBB自建渠道，再推荐至用户
                    if (channel.isJbbChannel()) {
                        if (test != null && test == true) {
                            return;
                        }
                        RegisterEvent event = new RegisterEvent(1, user.getUserId(), channelCode);
                        MqClient.send(Queues.JBB_MGT_USER_REGISTER_QUEUE_ADDR, Mapper.toBytes(event), 0);
                    }

                    txManager.commit(txStatus);
                    txStatus = null;
                } finally {
                    // roll back not committed transaction (release user lock)
                    rollbackTransaction(txStatus);
                }
            }

            // 创建用户Key
            createData(user, Constants.ONE_DAY_MILLIS);

        } else {
            throw new WrongParameterValueException("jbb.mgt.error.exception.wrongMsgCode");
        }
        logger.debug("<createUser()");
    }

    private void insertApplyRecord(User user, Account creator, Integer initAccountId) {

        // 从H5链接进来,组织存在,再把这个这个用户插入到mgt_user_apply_records
        if (creator.getOrgId() > 0) {
            UserApplyRecord userApplyRecord = new UserApplyRecord();
            userApplyRecord.setUserId(user.getUserId());
            userApplyRecord.setOrgId(creator.getOrgId());
            userApplyRecord.setCreationDate(DateUtil.getCurrentTimeStamp());
            userApplyRecord.setStatus(1);

            // initAccountId判断是否从电销,中介链接过来
            if (initAccountId != null && initAccountId > 0) {

                // 检查这份审核人是否存在,是否与渠道创建人为一个组织
                Account account = coreAccountService.getAccountById(initAccountId, null, false);
                if (account != null && account.getOrgId() == creator.getOrgId()) {
                    userApplyRecord.setAssignAccId(initAccountId);
                    userApplyRecord.setAssingDate(DateUtil.getCurrentTimeStamp());
                    userApplyRecord.setInitAccId(initAccountId);
                    userApplyRecord.setStatus(2);
                } else {
                    throw new WrongParameterValueException("jbb.mgt.exception.apply.initOrOrgIdError");
                }
            }
            userApplyRecordService.createUserApplyRecord(userApplyRecord);
        }
    }

    private void createData(User user, long expiry) {
        int userId = user.getUserId();
        //
        int applicationId = User.USER_APPLICATION_ID;

        UserKey userKey = user.getKey();
        boolean createNewKey = (userKey == null);

        if (!createNewKey) {
            Timestamp keyExpiry = userKey.getExpiry();
            createNewKey =
                (keyExpiry == null) || (keyExpiry.getTime() < System.currentTimeMillis() + Constants.ONE_DAY_MILLIS);
        }

        if (createNewKey) {
            // create new user key
            userKey = userService.createUserKey(userId, applicationId, expiry, true);
        }

        response.key = userKey.getUserKey();
        Timestamp keyExpiry = userKey.getExpiry();
        response.keyExpire = StringUtil.printDateTime(keyExpiry, DateUtil.getCurrentCalendar());
        response.keyExpireMs = keyExpiry.getTime();

    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private String key;
        private String keyExpire;
        private long keyExpireMs;

        private List<RsLoginLog> loginLogs;

        public String getKey() {
            return key;
        }

        public String getKeyExpire() {
            return keyExpire;
        }

        public long getKeyExpireMs() {
            return keyExpireMs;
        }

        public List<RsLoginLog> getLoginLogs() {
            return loginLogs;
        }

    }

}
