package com.jbb.mgt.rs.action.channel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.jbb.mgt.core.domain.ChannelStatistic;
import com.jbb.mgt.core.domain.Organization;
import com.jbb.mgt.core.service.OrganizationService;
import com.jbb.mgt.server.core.util.PasswordUtil;
import com.jbb.server.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jbb.mgt.core.domain.Account;
import com.jbb.mgt.core.domain.Channel;
import com.jbb.mgt.core.service.ChannelService;
import com.jbb.mgt.server.rs.action.BasicAction;
import com.jbb.mgt.server.rs.pojo.ActionResponse;
import com.jbb.mgt.server.rs.pojo.RsAccount;
import com.jbb.mgt.server.rs.pojo.RsChannel;
import com.jbb.server.common.exception.AccessDeniedException;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.exception.ObjectNotFoundException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.CommonUtil;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.common.util.StringUtil;
import com.jbb.server.shared.rs.Util;

/**
 * 渠道操作action
 * 
 * @author wyq
 * @date 2018/04/25
 */
@Service(ChannelAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ChannelAction extends BasicAction {

    public static final String ACTION_NAME = "ChannelAction";

    private static Logger logger = LoggerFactory.getLogger(ChannelAction.class);

    @Autowired
    private ChannelService channelService;

    @Autowired
    private OrganizationService organizationService;

    private Response response;

    @Override
    public ActionResponse makeActionResponse() {
        return this.response = new Response();
    }

    public void createChannel(Request req) {
        logger.debug(">createChannel, channle={}", req);
        Channel channel = generateChannel(null, req);
        // 检查渠道code 是否存在
        Channel channelByCode = channelService.getChannelByCode(channel.getChannelCode());
        if (null != channelByCode) {
            throw new MissingParameterException("jbb.error.exception.duplicateChannel", "zh", "channelCode");
        }

        // 检查账户是否被渠道占用
        Channel chnnelE = channelService.getChannelBySourcePhoneNumber(channel.getSourcePhoneNumber());
        if (chnnelE != null) {
            throw new WrongParameterValueException("jbb.mgt.exception.channel.PhonenumberError");
        }

        validateRequest(channel);

        if (StringUtil.isEmpty(req.channelCode)) {
            channel.setChannelCode(StringUtil.randomAlphaNum(6));
        }
        channel.setCreationDate(DateUtil.getCurrentTimeStamp());
        channel.setChannelUrl(Constants.CHANNELURL + channel.getChannelCode());
        if (null != channel.getSourcePassword()) {
            channel.setSourcePassword(PasswordUtil.passwordHash(channel.getSourcePassword()));
        }
        channel.setCreator(this.account.getAccountId());
        try {
            channelService.createChannal(channel);
        } catch (DuplicateKeyException e) {
            // 重新尝试一次
            channel.setChannelCode(StringUtil.randomAlphaNum(6));
            channelService.createChannal(channel);
        }
        this.response.channelCode = channel.getChannelCode();
        logger.debug("<createChannel");
    }

    public void getChannels(String searchText, String startDate, String endDate) {
        logger.debug(">getChannels");
        if (StringUtil.isEmpty(searchText)) {
            searchText = null;
        }
        if (StringUtil.isEmpty(startDate)) {
            startDate = null;
        }
        if (StringUtil.isEmpty(endDate)) {
            endDate = null;
        }
        List<Channel> channels = channelService.getChannels(searchText, this.account.getAccountId(),
            Util.parseTimestamp(startDate, getTimezone()), Util.parseTimestamp(endDate, getTimezone()));

        if (!CommonUtil.isNullOrEmpty(channels)) {
            this.response.channels = new ArrayList<RsChannel>(channels.size());
            for (Channel channel : channels) {
                RsChannel rsChannel = new RsChannel(channel);
                this.response.channels.add(rsChannel);
            }

        }
        logger.debug("<getChannels");
    }

    public void getChannelByCode(String channelCode) {
        logger.debug(">getChannelByCode, channelCode=" + channelCode);
        if (StringUtil.isEmpty(channelCode)) {
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "channelCode");
        }
        Channel channel = channelService.getChannelByCode(channelCode);
        if (channel != null) {
            Organization org = organizationService.getOrganizationById(channel.getAccount().getOrgId());
            this.response.orgName = org.getName();
        }
        /*if (channel == null) {
            throw new MissingParameterException("jbb.error.exception.channelFrozenOrDelete", "zh", "channelCode");
        }*/
        RsChannel rs = new RsChannel();
        Account cAccount = null;
        if (this.account == null && null != channel) {
            rs.setChannelCode(channel.getChannelCode());
            rs.setChannelName(channel.getChannelName());
            rs.setChannelUrl(channel.getChannelUrl());
            rs.setServiceQQ(channel.getServiceQQ());
            rs.setServiceWechat(channel.getServiceWechat());
            rs.setCreationDate(Util.getTimeMs(channel.getCreationDate()));
            rs.setQqRequired(channel.isQqRequired());
            rs.setWechatRequired(channel.isWechatRequired());
            rs.setZhimaRequired(channel.isZhimaRequired());
            rs.setIdcardInfoRequired(channel.isIdcardInfoRequired());
            rs.setIdcardBackRequired(channel.isIdcardBackRequired());
            rs.setIdcardRearRequired(channel.isIdcardRearRequired());
            rs.setHeaderRequired(channel.isHeaderRequired());
            rs.setMobileContract1Required(channel.isMobileContract1Required());
            rs.setMobileContract2Required(channel.isMobileContract2Required());
            rs.setMobileServiceInfoRequired(channel.isMobileServiceInfoRequired());
            rs.setReceiveMode(channel.getReceiveMode());
            rs.setHidden(channel.isHidden());
            rs.setTaobaoRequired(channel.isTaobaoRequired());
            rs.setJingdongRequired(channel.isJingdongRequired());
            rs.setGjjRequired(channel.isGjjRequired());
            rs.setSjRequired(channel.isSjRequired());
            rs.setChsiRequired(channel.isChsiRequired());
            cAccount = this.coreAccountService.getAccountById(channel.getCreator(), null, false);
            RsAccount rsAccount = new RsAccount(new Account());
            rsAccount.setOrgId(cAccount.getOrgId());
            rs.setCreator(rsAccount);
        } else if (this.account != null && null != channel) {
            // 没有登录的时候不需要验证
            /*validateOpRight(channel);
            cAccount = this.coreAccountService.getAccountById(channel.getCreator(), this.account.getOrgId(), false);*/
            cAccount = this.coreAccountService.getAccountById(channel.getCreator(), null, false);
            rs = new RsChannel(channel);
            rs.setCreator(new RsAccount(cAccount));
        }
        this.response.channel = rs;
        logger.debug("<getChannelByCode");
    }

    public void checkChannelPhoneNumberAndPassword(String sourcePhoneNumber, String sourcePassword) {
        logger.debug(">checkChannelPhoneNumberAndPassword(), sourcePhoneNumber=" + sourcePhoneNumber);
        channelService.checkPhoneNumberAndPassword(sourcePhoneNumber, sourcePassword);
        logger.debug("<checkChannelPhoneNumberAndPassword(), sourcePhoneNumber=" + sourcePhoneNumber);
    }

    public void updateChannelByCode(String channelCode, Request req) {
        logger.debug(">updateChannelByCode");
        if (StringUtil.isEmpty(channelCode)) {
            throw new WrongParameterValueException("jbb.mgt.error.exception.missing.param", "zh", "channelCode");
        }
        // 组织管理员和渠道所属市场员 可以编辑渠道
        Channel channel = channelService.getChannelByCode(channelCode);
        if (channel == null) {
            channel = generateChannel(channel, req);
            // 验证字段
            validateRequest(channel);

            // 检查账户是否被渠道占用
            Channel channelByCode = channelService.getChannelBySourcePhoneNumber(channel.getSourcePhoneNumber());
            if (channelByCode != null) {
                throw new WrongParameterValueException("jbb.mgt.exception.channel.PhonenumberError");
            }
            channel.setCreator(this.account.getAccountId());
            channel.setChannelCode(channelCode);
            channel.setSourcePassword(PasswordUtil.passwordHash(channel.getSourcePassword()));
            channel.setChannelUrl(Constants.CHANNELURL + channel.getChannelCode());
            channelService.createChannal(channel);
        } else {
            validateOpRight(channel);
            channel = generateChannel(channel, req);
            // 验证字段
            validateRequest(channel);
            channel.setSourcePassword(PasswordUtil.passwordHash(channel.getSourcePassword()));
            channel.setChannelUrl(Constants.CHANNELURL + channel.getChannelCode());
            // 更新
            channelService.updateChannle(channel);
        }
        logger.debug("<updateChannelByCode");
    }

    public void deleteChannelInfo(String channelCode) {
        logger.debug(">deleteChannelInfo, channelCode=" + channelCode);
        if (StringUtil.isEmpty(channelCode)) {
            throw new WrongParameterValueException("jbb.mgt.error.exception.missing.param", "zh", "channelCode");
        }
        // 组织管理员和渠道所属市场员 可以编辑渠道
        Channel channel = channelService.getChannelByCode(channelCode);
        if (channel == null) {
            throw new MissingParameterException("jbb.error.exception.channelFrozenOrDelete", "zh", "channelCode");
        }
        validateOpRight(channel);

        channelService.deleteChannel(channelCode);
        logger.debug("<deleteChannelInfo");
    }

    public void freezeChannelInfo(String channelCode) {
        logger.debug(">freezeChannelInfo, channelCode=" + channelCode);
        if (StringUtil.isEmpty(channelCode)) {
            throw new WrongParameterValueException("jbb.mgt.error.exception.missing.param", "zh", "channelCode");
        }
        // 组织管理员和渠道所属市场员 可以编辑渠道
        Channel channel = channelService.getChannelByCode(channelCode);
        if (channel == null) {
            throw new MissingParameterException("jbb.error.exception.channelFrozenOrDelete", "zh", "channelCode");
        }
        validateOpRight(channel);

        channelService.frozeChannel(channelCode);
        logger.debug("<freezeChannelInfo");
    }

    public void thawChannelInfo(String channelCode) {
        logger.debug(">thawChannelInfo, channelCode=" + channelCode);
        if (StringUtil.isEmpty(channelCode)) {
            throw new WrongParameterValueException("jbb.mgt.error.exception.missing.param", "zh", "channelCode");
        }
        // 组织管理员和渠道所属市场员 可以编辑渠道
        Channel channel = channelService.getChannelByCode(channelCode);
        if (channel == null) {
            throw new MissingParameterException("jbb.mgt.exception.channel.notFound");
        }
        validateOpRight(channel);

        channelService.thawChannel(channelCode);
        logger.debug("<thawChannelInfo");
    }

    public void selectChannelStatisticS(String channelCode, String startDate, String endDate) {
        logger.debug(
            ">selectChannelStatisticS, channelCode=" + channelCode + "startDate=" + startDate + "endDate=" + endDate);
        Integer accountId = null;
        if (StringUtil.isEmpty(channelCode)) {
            channelCode = null;
            accountId = this.account.getAccountId();
        } else {
            Channel channel = channelService.getChannelByCode(channelCode);
            if (null == channel || channel.getStatus() == 1 || channel.isHidden()) {
                throw new MissingParameterException("jbb.mgt.exception.channel.notFound");
            } else {
//                validateOpRight(channel);
                if (!(channel.getAccount().getOrgId() == this.account.getOrgId() || this.account.getOrgId() == 1)){
                    throw new MissingParameterException("jbb.mgt.exception.notAcrossTheOrganization");
                }
            }
        }
        if (StringUtil.isEmpty(startDate)) {
            startDate = null;
        }
        if (StringUtil.isEmpty(endDate)) {
            endDate = null;
        }
        List<ChannelStatistic> list = channelService.selectChannelStatisticS(channelCode, accountId,
            Util.parseTimestamp(startDate, getTimezone()), Util.parseTimestamp(endDate, getTimezone()));
        this.response.statistic = new ChannelStatistic();
        for (ChannelStatistic cs : list) {
            this.response.statistic.setUvCnt(this.response.statistic.getUvCnt() + cs.getUvCnt());
            this.response.statistic.setSubmitCnt(this.response.statistic.getSubmitCnt() + cs.getSubmitCnt());
            this.response.statistic.setClickCnt(this.response.statistic.getClickCnt() + cs.getClickCnt());
            this.response.statistic.setLoanCnt(this.response.statistic.getLoanCnt() + cs.getLoanCnt());
            this.response.statistic.setRegisterCnt(this.response.statistic.getRegisterCnt() + cs.getRegisterCnt());
        }
        logger.debug("<selectChannelStatisticS");
    }

    private void validateOpRight(Channel channel) {
        if (!(channel.getCreator() == this.account.getAccountId() || this.isOrgAdmin())) {
            throw new AccessDeniedException("jbb.mgt.exception.accountAccessDenied");
        }
    }

    private void validateRequest(Channel channel) {
        if (StringUtil.isEmpty(channel.getChannelName())) {
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "channelName");
        }
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class Response extends ActionResponse {
        private String channelCode;

        private RsChannel channel;

        private List<RsChannel> channels;

        private String orgName;

        private ChannelStatistic statistic;

        public String getChannelCode() {
            return channelCode;
        }

        public RsChannel getChannel() {
            return channel;
        }

        public List<RsChannel> getChannels() {
            return channels;
        }

        public String getOrgName() {
            return orgName;
        }

        public ChannelStatistic getStatistic() {
            return statistic;
        }
    }

    private Channel generateChannel(Channel channel, Request req) {
        channel = channel == null ? new Channel() : channel;
        if (!StringUtil.isEmpty(req.channelCode)) {
            channel.setChannelCode(req.channelCode);
        }
        channel.setChannelName(req.channelName);
        channel.setChannelUrl(req.channelUrl);
        channel.setServiceQQ(req.serviceQQ);
        channel.setServiceWechat(req.serviceWechat);
        channel.setMode(req.mode);
        if (!StringUtil.isEmpty(req.sourcePhoneNumber)) {
            channel.setSourcePhoneNumber(req.sourcePhoneNumber);
        }

        if (!StringUtil.isEmpty(req.sourcePassword)) {
            channel.setSourcePassword(req.sourcePassword);
        }

        channel.setQqRequired(req.qqRequired);
        channel.setWechatRequired(req.wechatRequired);
        channel.setZhimaRequired(req.zhimaRequired);
        channel.setIdcardInfoRequired(req.idcardInfoRequired);
        channel.setIdcardBackRequired(req.idcardBackRequired);
        channel.setIdcardRearRequired(req.idcardRearRequired);
        channel.setHeaderRequired(req.headerRequired);
        channel.setMobileContract1Required(req.mobileContract1Required);
        channel.setMobileContract2Required(req.mobileContract2Required);
        channel.setMobileServiceInfoRequired(req.mobileServiceInfoRequired);
        channel.setCpaPrice(req.cpaPrice);
        channel.setCpsPrice(req.cpsPrice);
        channel.setReceiveMode(req.receiveMode);
        channel.setHidden(req.hidden);
        channel.setTaobaoRequired(req.taobaoRequired);
        channel.setJingdongRequired(req.jingdongRequired);
        channel.setGjjRequired(req.gjjRequired);
        channel.setSjRequired(req.sjRequired);
        channel.setChsiRequired(req.chsiRequired);
        return channel;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Request {
        public String channelCode;
        public String channelName;
        public String channelUrl;
        public String serviceQQ;
        public String serviceWechat;
        public int mode;
        public String sourcePhoneNumber;
        public String sourcePassword;
        public boolean qqRequired;
        public boolean wechatRequired;
        public boolean zhimaRequired;
        public boolean idcardInfoRequired;
        public boolean idcardBackRequired;
        public boolean idcardRearRequired;
        public boolean headerRequired;
        public boolean mobileContract1Required;
        public boolean mobileContract2Required;
        public boolean mobileServiceInfoRequired;
        public int cpaPrice;
        public int cpsPrice;
        public int receiveMode;
        public boolean hidden;
        public boolean taobaoRequired;
        public boolean jingdongRequired;
        public boolean gjjRequired;
        public boolean sjRequired;
        public boolean chsiRequired;
    }

}
