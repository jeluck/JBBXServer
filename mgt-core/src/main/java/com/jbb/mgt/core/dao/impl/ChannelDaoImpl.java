package com.jbb.mgt.core.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.mgt.core.domain.ChannelStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.mgt.core.dao.ChannelDao;
import com.jbb.mgt.core.dao.mapper.ChannelMapper;
import com.jbb.mgt.core.domain.Channel;

/**
 * 渠道操作dao实现类
 * 
 * @author wyq
 * @date 2018/04/25
 */
@Repository("ChannelDao")
public class ChannelDaoImpl implements ChannelDao {

    @Autowired
    private ChannelMapper mapper;

    @Override
    public void insertChannal(Channel channel) {
        mapper.insertChannal(channel);
    }

    @Override
    public Channel selectChannelByCode(String channelCode) {
        return mapper.selectChannelByCode(channelCode);
    }

    @Override
    public List<Channel> selectChannels(String searchText, int accountId, Timestamp startDate, Timestamp endDate) {
        return mapper.selectChannels(searchText, accountId, startDate, endDate);
    }

    @Override
    public void updateChannle(Channel channel) {
        mapper.updateChannel(channel);

    }

    @Override
    public boolean deleteChannel(String channelCode) {
        return mapper.deleteChannel(channelCode) > 0;
    }

    @Override
    public void frozeChannel(String channelCode) {
        mapper.frozeChannel(channelCode);
    }

    @Override
    public void thawChannel(String channelCode) {
        mapper.thawChannel(channelCode);
    }

    @Override
    public Channel selectChannelBySourcePhoneNumber(String sourcePhoneNumber) {
        return mapper.selectChannelBySourcePhoneNumber(sourcePhoneNumber);
    }

    @Override
    public List<ChannelStatistic> selectChannelStatisticS(String channelCode, Integer accountId, Timestamp startDate,
        Timestamp endDate) {
        return mapper.selectChannelStatisticS(channelCode, accountId, startDate, endDate);
    }

}
