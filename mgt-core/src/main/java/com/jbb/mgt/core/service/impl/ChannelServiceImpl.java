package com.jbb.mgt.core.service.impl;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.mgt.core.domain.ChannelStatistic;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.mgt.core.dao.ChannelDao;
import com.jbb.mgt.core.domain.Channel;
import com.jbb.mgt.core.service.ChannelService;
import com.jbb.mgt.server.core.util.PasswordUtil;
import com.jbb.server.common.exception.AccessDeniedException;
import com.jbb.server.common.exception.MissingParameterException;
import com.jbb.server.common.exception.ObjectNotFoundException;
import com.jbb.server.common.exception.WrongParameterValueException;
import com.jbb.server.common.util.StringUtil;

/**
 * 渠道操作Service实现类
 *
 * @author wyq
 * @dete 2018年4月20日 10:38:16
 *
 */
@Service("ChannelService")
public class ChannelServiceImpl implements ChannelService {

    private static Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);

    @Autowired
    private ChannelDao channelDao;

    @Override
    public void createChannal(Channel channel) {
        channelDao.insertChannal(channel);
    }

    @Override
    public Channel getChannelByCode(String channelCode) {

        return channelDao.selectChannelByCode(channelCode);
    }

    @Override
    public List<Channel> getChannels(String searchText, int accountId, Timestamp startDate, Timestamp endDate) {

        return channelDao.selectChannels(searchText, accountId, startDate, endDate);
    }

    @Override
    public void updateChannle(Channel channel) {
        channelDao.updateChannle(channel);

    }

    @Override
    public void deleteChannel(String channelCode) {
        channelDao.deleteChannel(channelCode);

    }

    @Override
    public void frozeChannel(String channelCode) {
        channelDao.frozeChannel(channelCode);
    }

    @Override
    public void verifyChannel(String channelCode) {
        if (StringUtils.isNotBlank(channelCode)) {
            Channel channel = getChannelByCode(channelCode);
            if (channel != null) {
                return;
            }
        }
        throw new WrongParameterValueException("jbb.mgt.error.exception.channelNotFound");
    }

    @Override
    public Channel checkPhoneNumberAndPassword(String phoneNumber, String passWord) {
        if (StringUtil.isEmpty(phoneNumber)) {
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "sourcePhoneNumber");
        }
        if (StringUtil.isEmpty(passWord)) {
            throw new MissingParameterException("jbb.mgt.error.exception.missing.param", "zh", "sourcePassword");
        }

        Channel channel = channelDao.selectChannelBySourcePhoneNumber(phoneNumber);
        if (channel == null) {
            throw new AccessDeniedException("jbb.mgt.exception.channel.userNameOrPwError");
        }

        if (!phoneNumber.equals(channel.getSourcePhoneNumber())
            || !PasswordUtil.verifyPassword(passWord, channel.getSourcePassword())) {
            throw new AccessDeniedException("jbb.mgt.exception.channel.userNameOrPwError");
        }
        return channel;
    }

    @Override
    public void thawChannel(String channelCode) {
        channelDao.thawChannel(channelCode);
    }

    @Override
    public Channel getChannelBySourcePhoneNumber(String sourcePhoneNumber) {
        return channelDao.selectChannelBySourcePhoneNumber(sourcePhoneNumber);
    }

    @Override
    public List<ChannelStatistic> selectChannelStatisticS(String channelCode, Integer accountId, Timestamp startDate,
        Timestamp endDate) {
        return channelDao.selectChannelStatisticS(channelCode, accountId, startDate, endDate);
    }
}
