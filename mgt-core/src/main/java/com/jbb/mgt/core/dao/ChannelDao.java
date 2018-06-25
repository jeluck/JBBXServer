package com.jbb.mgt.core.dao;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.mgt.core.domain.Channel;
import com.jbb.mgt.core.domain.ChannelStatistic;

/**
 * 渠道操作dao借口
 * 
 * @author wyq
 * @date 2018年4月20日 10:55:59
 *
 */
public interface ChannelDao {

    void insertChannal(Channel channel);

    Channel selectChannelByCode(String channelCode);

    Channel selectChannelBySourcePhoneNumber(String sourcePhoneNumber);

    List<Channel> selectChannels(String searchText, int accountId, Timestamp startDate, Timestamp endDate);

    void updateChannle(Channel channel);

    boolean deleteChannel(String channelCode);

    public void frozeChannel(String channelCode);

    void thawChannel(String channelCode);

    List<ChannelStatistic> selectChannelStatisticS(String channelCode, Integer accountId, Timestamp startDate,
        Timestamp endDate);
}
