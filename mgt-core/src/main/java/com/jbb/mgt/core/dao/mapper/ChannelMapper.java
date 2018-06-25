package com.jbb.mgt.core.dao.mapper;

import java.sql.Timestamp;
import java.util.List;

import com.jbb.mgt.core.domain.ChannelStatistic;
import org.apache.ibatis.annotations.Param;

import com.jbb.mgt.core.domain.Channel;

/**
 * 渠道操作mapper类
 * 
 * @author wyq
 * @date 2018年4月20日 11:46:54
 *
 */
public interface ChannelMapper {

    void insertChannal(Channel channel);

    Channel selectChannelByCode(@Param(value = "channelCode") String channelCode);

    Channel selectChannelBySourcePhoneNumber(@Param(value = "sourcePhoneNumber") String sourcePhoneNumber);

    List<Channel> selectChannels(@Param(value = "searchText") String searchText,
        @Param(value = "creator") int accountid, @Param(value = "startDate") Timestamp startDate,
        @Param(value = "endDate") Timestamp endDate);

    void updateChannel(Channel channel);

    int deleteChannel(@Param(value = "channelCode") String channelCode);

    void frozeChannel(@Param(value = "channelCode") String channelCode);

    List<Channel> selectChannelsByNameOrCreator(@Param(value = "channelName") String channelNameOrCreator);

    void thawChannel(@Param(value = "channelCode") String channelCode);

    List<ChannelStatistic> selectChannelStatisticS(@Param(value = "channelCode") String channelCode,
        @Param(value = "accountId") Integer accountId, @Param(value = "startDate") Timestamp startDate,
        @Param(value = "endDate") Timestamp endDate);
}
