package com.jbb.mgt.server.rs.pojo;

import com.jbb.mgt.core.domain.Channel;

/**
 * @author wyq
 * @date 2018/6/9 20:12
 */
public class RsUserApplyRecordChannel {
    private String ChannelCode;
    private String ChannelName;

    public RsUserApplyRecordChannel() {
        super();
    }

    public RsUserApplyRecordChannel(Channel Channel) {
        this.ChannelCode = Channel.getChannelCode();
        this.ChannelName = Channel.getChannelName();
    }

    public String getChannelCode() {
        return ChannelCode;
    }

    public void setChannelCode(String channelCode) {
        ChannelCode = channelCode;
    }

    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
    }
}
