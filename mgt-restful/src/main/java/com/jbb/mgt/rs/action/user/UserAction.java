package com.jbb.mgt.rs.action.user;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.jbb.mgt.core.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jbb.mgt.core.domain.Account;
import com.jbb.mgt.core.domain.AccountOpLog;
import com.jbb.mgt.core.domain.Channel;
import com.jbb.mgt.core.domain.IPAddressInfo;
import com.jbb.mgt.core.domain.User;
import com.jbb.mgt.core.domain.UserApplyRecord;
import com.jbb.mgt.core.domain.UserKey;
import com.jbb.mgt.core.domain.mapper.Mapper;
import com.jbb.mgt.core.domain.mq.RegisterEvent;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.server.common.Constants;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.DigitUtil;
import com.jbb.server.common.util.IDCardUtil;
import com.jbb.server.common.util.PhoneNumberUtil;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.mq.MqClient;
import com.jbb.server.mq.Queues;
import com.jbb.server.shared.rs.Util;

/**
 * 借款用户Action
 * <p>
 * author 姜刘鸿
 *
 * @date 2018/4/26
 */
@Service(UserAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserAction extends BasicAction {

    public static final String ACTION_NAME = "UserAction";

    private static Logger logger = LoggerFactory.getLogger(UserAction.class);

    private Response response;

    @Autowired
    @Qualifier("UserService")
    private UserService userService;
    @Autowired
    @Qualifier("ChannelService")
    private ChannelService channelService;
    @Autowired
    @Qualifier("UserApplyRecordService")
    private UserApplyRecordService userApplyRecordService;
    @Autowired
    @Qualifier("AreaZonesService")
    private AreaZonesService areaZonesService;
    @Autowired
    @Qualifier("AccountOpLogService")
    private AccountOpLogService AccountOpLogService;

    @Autowired
    private AliyunService aliyunService;

    @Autowired
    private UserEventLogService userEventLogService;

    @Override
    protected ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    public void createUser(String channelCode, Integer step, Boolean test, Request request) {
        logger.debug(">createUser", request);
        Channel channel = channelService.getChannelByCode(channelCode);
        if (channel.getStatus() == 1 || channel.getStatus() == 2) {
            throw new MissingParameterException("jbb.error.exception.channelFrozenOrDelete");
        }
        User user = generateUserForEdit(this.user, channel, request);

        IPAddressInfo ipAddressInfo = aliyunService.getIPAddressInfo(this.getRemoteAddress());
        if (ipAddressInfo != null) {
            user.setIpArea(ipAddressInfo.getIpArea());
        }

        userService.updateUser(user);

        boolean testF = (test != null && test == true);
        if (testF) {
            // 测试账户不推荐
            return;
        }

        if (channel.isJbbChannel() && step != null) {
            // 如果是JBB的渠道，进入帮帮导流
            RegisterEvent event = new RegisterEvent(step, user.getUserId(), channelCode);
            MqClient.send(Queues.JBB_MGT_USER_REGISTER_QUEUE_ADDR, Mapper.toBytes(event), 0);
        }

        logger.debug("<createUser");
    }

    /**
     * 获取渠道列表
     * 
     * @param sourcePhoneNumber 账户
     * @param sourcePassword 密码
     * @param startDate 开始时间
     * @param endDate 结束时间
     */
    public void getUserList(int pageNo, int pageSize, String sourcePassword, String sourcePhoneNumber, String startDate,
        String endDate) {
        logger.debug(">getUserList(),sourcePhoneNumber=" + sourcePhoneNumber + ",sourcePassword=" + sourcePassword
            + ",startDate=" + startDate + ",endDate=" + endDate);
        Channel channel = channelService.checkPhoneNumberAndPassword(sourcePhoneNumber, sourcePassword);

        Timestamp tsStartDate = StringUtil.isEmpty(startDate) ? null : Util.parseTimestamp(startDate, getTimezone());
        Timestamp tsEndDate = StringUtil.isEmpty(endDate) ? null : Util.parseTimestamp(endDate, getTimezone());

        PageHelper.startPage(pageNo, pageSize);
        List<User> list = userService.selectUsers(sourcePhoneNumber, sourcePassword, channel.getChannelCode(),
            tsStartDate, tsEndDate);

        this.response.todayCnt = userService.countUserByChannelCode(channel.getChannelCode(),
            DateUtil.getCurrentTimeStamp(), DateUtil.getCurrentTimeStamp());
        this.response.totalCnt = userService.countUserByChannelCode(channel.getChannelCode(), null, null);

        PageInfo<User> pageInfo = new PageInfo<User>(list);
        this.response.pageInfo = pageInfo;

        PageHelper.clearPage();
        logger.debug("<getUserList()");
    }

    // 获取渠道用户明细
    public void getUserDetails(int pageNo, int pageSize, String channelCode, String startDate, String endDate,
        Boolean isGetJbbChannels) {
        logger.debug(">getUserDetails(),pageNo=" + pageNo + ",pageSize=" + pageSize + ",startDate=" + startDate
            + ",endDate=" + endDate);
        Boolean GetJbbChannels = isGetJbbChannels == null ? false : isGetJbbChannels;

        Timestamp tsStartDate = StringUtil.isEmpty(startDate) ? null : Util.parseTimestamp(startDate, getTimezone());
        Timestamp tsEndDate = StringUtil.isEmpty(endDate) ? null : Util.parseTimestamp(endDate, getTimezone());

        PageHelper.startPage(pageNo, pageSize);
        Integer orgId = this.account.getOrgId();
        List<User> list = userService.selectUserDetails(channelCode, tsStartDate, tsEndDate, orgId, GetJbbChannels);

        PageInfo<User> pageInfo = new PageInfo<User>(list);
        this.response.pageInfo = pageInfo;

        PageHelper.clearPage();
        logger.debug("<getUserDetails(),pageNo=" + pageNo + ",pageSize=" + pageSize + ",startDate=" + startDate
            + ",endDate=" + endDate);

    }

    private User generateUserForEdit(User user, Channel channel, Request request) {

        if (channel.isIdcardInfoRequired() && StringUtil.isEmpty(request.username)) {
            throw new MissingParameterException("jbb.mgt.exception.username.empty");
        }
        /* if (StringUtil.isEmpty(request.username)) {
             throw new MissingParameterException("jbb.error.exception.wrong.paramvalue", "zh", "username");
         } else if (!ChineseNameUtil.checkChinese(request.username)) {
             throw new WrongParameterValueException("jbb.error.exception.wrong.paramvalue", "zh", "username");
         }
        }*/

        if (channel.isIdcardInfoRequired()) {
            if (StringUtil.isEmpty(request.idcard) || !IDCardUtil.validate(request.idcard)) {
                throw new MissingParameterException("jbb.mgt.exception.idcard.error");
            }

        } else if (!StringUtil.isEmpty(request.idcard) && !IDCardUtil.validate(request.idcard)) {
            throw new WrongParameterValueException("jbb.mgt.exception.idcard.error");
        }
        user.setUserName(request.username);
        user.setIdCard(request.idcard);

        if (channel.isQqRequired() && StringUtil.isEmpty(request.qq)) {
            throw new MissingParameterException("jbb.mgt.exception.qq.empty");
        }
        user.setQq(request.qq);

        if (channel.isWechatRequired() && StringUtil.isEmpty(request.wechat)) {
            throw new MissingParameterException("jbb.mgt.exception.wechat.empty");
        }
        user.setWechat(request.wechat);
        if (channel.isZhimaRequired()) {
            if (!DigitUtil.checkDigit(request.zhimaScore)) {
                throw new WrongParameterValueException("jbb.mgt.exception.zhima.error");
            }
        }
        user.setZhimaScore(request.zhimaScore);
        if (channel.isIdcardRearRequired()) {
            if (StringUtil.isEmpty(request.idcardRear)) {
                throw new MissingParameterException("jbb.mgt.exception.idcardRear.empty");
            }
        }
        user.setIdcardRear(request.idcardRear);
        if (channel.isIdcardBackRequired()) {
            if (StringUtil.isEmpty(request.idcardBack)) {
                throw new MissingParameterException("jbb.mgt.exception.idcardBack.empty");
            }
        }
        user.setIdcardBack(request.idcardBack);
        if (channel.isHeaderRequired()) {
            if (StringUtil.isEmpty(request.idcardInfo)) {
                throw new MissingParameterException("jbb.mgt.exception.idcardInfo.empty");
            }
        }
        user.setIdcardInfo(request.idcardInfo);
        if (channel.isMobileContract1Required()) {
            if (StringUtil.isEmpty(request.contract1Username)
                || !PhoneNumberUtil.isValidPhoneNumber(request.contract1Phonenumber)) {
                throw new MissingParameterException("jbb.mgt.exception.usernameorphone1.empty");
            }
            // 判断紧急联系人1名字是否为中文
            /* if (!ChineseNameUtil.checkChinese(request.contract1Username)) {
                throw new WrongParameterValueException("jbb.error.exception.wrong.paramvalue", "zh",
                    "contract1Username");
            }*/
            // 判断紧急联系人1手机号是否合法
        } else if (!StringUtil.isEmpty(request.contract1Phonenumber)
            && !PhoneNumberUtil.isValidPhoneNumber(request.contract1Phonenumber)) {
            throw new WrongParameterValueException("jbb.mgt.exception.contract1Phonenumber.error");
        }
        user.setContract1Relation(request.contract1Relation);
        user.setContract1Username(request.contract1Username);
        user.setContract1Phonenumber(request.contract1Phonenumber);
        if (channel.isMobileContract2Required()) {
            if (StringUtil.isEmpty(request.contract2Username)
                || !PhoneNumberUtil.isValidPhoneNumber(request.contract2Phonenumber)
                || request.contract2Phonenumber.equals(request.contract1Phonenumber)) {
                throw new MissingParameterException("jbb.mgt.exception.usernameorphone2.empty");
            }
            // 判断紧急联系人2的名字是否为中文 是否跟联系人1相同
            /*   if (!ChineseNameUtil.checkChinese(request.contract2Username)) {
                throw new WrongParameterValueException("jbb.error.exception.wrong.paramvalue", "zh",
                    "contract2Username");
            } else if (request.contract2Username.equals(request.contract1Username)) {
                throw new WrongParameterValueException("jbb.error.exception.wrong.paramvalue", "zh",
                    "contractUsername");
            }*/
            // 判断紧急联系人2的手机号是否合法 是否跟联系人1相同
        } else if (!StringUtil.isEmpty(request.contract2Phonenumber)
            && !PhoneNumberUtil.isValidPhoneNumber(request.contract2Phonenumber)) {
            throw new WrongParameterValueException("jbb.mgt.exception.contract2Phonenumber.error");
        } else if (!StringUtil.isEmpty(request.contract2Phonenumber)
            && request.contract2Phonenumber.equals(request.contract1Phonenumber)) {
            throw new WrongParameterValueException("jbb.mgt.exception.Phonenumber.same");
        }
        user.setContract2Relation(request.contract2Relation);
        user.setContract2Username(request.contract2Username);
        user.setContract2Phonenumber(request.contract2Phonenumber);
        return user;
    }

    /**
     * 返回用户补充资料链接
     *
     * @param applyId
     */
    public void getUserAddDataH5Url(Integer applyId, Boolean refresh) {
        boolean refreshFlag = (refresh != null && refresh == true);
        if (null == applyId || applyId == 0) {
            throw new WrongParameterValueException("jbb.mgt.error.exception.missing.param", "zh", "applyId");
        }
        UserApplyRecord userApplyRecord = userApplyRecordService.selectUserApplyRecordInfoByApplyId(applyId);
        if (null == userApplyRecord) {
            throw new WrongParameterValueException("jbb.mgt.error.exception.wrong.paramvalue", "zh", "applyId");
        }

        UserKey userKey =
            userService.selectUserKeyByUserIdAndAppId(userApplyRecord.getUserId(), User.ADDINFO_APPLICATION_ID);

        boolean isNullUserKey = (userKey == null);
        if (userKey == null && !refreshFlag) {
            // 不返回任何信息
            return;
        }

        if (refreshFlag) {
            userKey = new UserKey();
            userKey.setUserKey(StringUtil.secureRandom(128).substring(0, 64));
            userKey.setExpiry(new Timestamp(DateUtil.getCurrentTime() + DateUtil.DAY_MILLSECONDES));
            userKey.setApplicationId(User.ADDINFO_APPLICATION_ID);
            userKey.setDeleted(false);
            userKey.setUserId(userApplyRecord.getUserId());
            if (!isNullUserKey) {
                userService.updateUserKey(userKey);
            } else {
                userService.insertUserKey(userKey);
            }
        }

        this.response.h5Url = PropertyManager.getProperty("jbb.mgt.org.h5.server.address") + "?s=cl"
            + userApplyRecord.getOrgId() + "&key=" + userKey.getUserKey();
        this.response.urlExpireMs = userKey.getExpiry().getTime();
    }

    /**
     * 查询操作日志
     *
     * @param applyId
     */
    public void getOplogs(Integer applyId) {
        if (null == applyId || applyId == 0) {
            throw new WrongParameterValueException("jbb.mgt.error.exception.missing.param", "zh", "applyId");
        }
        List<AccountOpLog> accountOpLogs = AccountOpLogService.selectAccountOpLogByApplyId(applyId);
        this.response.opLogs = new ArrayList<>();
        this.response.opLogs.addAll(accountOpLogs);

    }

    /**
     * 验证渠道中要求的字段是否有值
     *
     * @param user
     */
    public void verifyChannel(User user) {
        Channel channel = channelService.getChannelByCode(user.getChannelCode());
        boolean b = false;
        if (null != channel) {
            if (channel.isQqRequired()) {
                if (!StringUtil.isEmpty(user.getQq())) {
                    b = true;
                }
            }
            if (channel.isWechatRequired()) {
                if (!StringUtil.isEmpty(user.getWechat())) {
                    b = true;
                }
            }
            if (channel.isZhimaRequired() == user.isZhimaVerified()) {
                if (null != user.getZhimaScore()) {
                    b = true;
                }
            }
            if (channel.isIdcardInfoRequired()) {
                if (null != user.getIdcardInfo()) {
                    b = true;
                }
            }
            if (channel.isIdcardBackRequired()) {
                if (null != user.getIdcardBack()) {
                    b = true;
                }
            }
            if (channel.isIdcardRearRequired()) {
                if (null != user.getIdcardRear()) {
                    b = true;
                }
            }
            if (channel.isMobileContract1Required()) {
                if (null != user.getContract1Username() || null != user.getContract1Relation()
                    || null != user.getContract1Phonenumber()) {
                    b = true;
                }
            }
            if (channel.isMobileContract2Required()) {
                if (null != user.getContract2Username() || null != user.getContract2Relation()
                    || null != user.getContract2Phonenumber()) {
                    b = true;
                }
            }
            if (channel.isMobileServiceInfoRequired() == user.isMobileVerified()) {
                b = true;
            }
            if (channel.isJingdongRequired() == user.isJingdongVerified()) {
                b = true;
            }
            if (channel.isSjRequired() == user.isSiVerified()) {
                b = true;
            }
            if (channel.isGjjRequired() == user.isGjjVerified()) {
                b = true;
            }
            if (channel.isChsiRequired() == user.isChsiVerified()) {
                b = true;
            }
        }
        if (b) {
            userEventLogService.insertEventLog(user.getUserId(), user.getChannelCode(), null, "fullInfo", null, null,
                null, null, DateUtil.getCurrentTimeStamp());
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Request {
        public String username;
        public String idcard;
        public int zhimaScore;
        public String qq;
        public String wechat;
        public String idcardRear;// 身份证正面
        public String idcardBack;// 身份证反面
        public String idcardInfo;// 手持身份证
        public String contract1Relation;// 联系人1关系
        public String contract1Username;// 联系人1名字
        public String contract1Phonenumber;// 联系人1联系方式
        public String contract2Relation;// 联系人2关系
        public String contract2Username;// 联系人2名字
        public String contract2Phonenumber;
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        public PageInfo<User> pageInfo;

        private Integer totalCnt;

        private Integer todayCnt;

        private String h5Url;
        private Long urlExpireMs;

        private List<AccountOpLog> opLogs;

        public List<AccountOpLog> getOpLogs() {
            return opLogs;
        }

        public String getH5Url() {
            return h5Url;
        }

        public Integer getTotalCnt() {
            return totalCnt;
        }

        public Integer getTodayCnt() {
            return todayCnt;
        }

        public PageInfo<User> getPageInfo() {
            return pageInfo;
        }

        public void setPageInfo(PageInfo<User> pageInfo) {
            this.pageInfo = pageInfo;
        }

        public Long getUrlExpireMs() {
            return urlExpireMs;
        }

    }

}
