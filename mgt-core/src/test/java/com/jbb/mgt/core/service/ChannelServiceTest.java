package com.jbb.mgt.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.jbb.mgt.core.domain.ChannelStatistic;
import com.jbb.server.common.util.DateUtil;
import org.apache.ibatis.jdbc.Null;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jbb.mgt.core.domain.Channel;
import com.jbb.server.common.Home;
import com.jbb.server.common.util.StringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})

public class ChannelServiceTest {

    @Autowired
    private ChannelService channelService;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testChannelService() {

        // 先删除所有测试数据
        List<Channel> all = channelService.getChannels("test",1, DateUtil.getCurrentTimeStamp(),DateUtil.getCurrentTimeStamp());
        for (Channel c : all) {
            channelService.deleteChannel(c.getChannelCode());
        }

        // 先插入数据
        Channel channel = new Channel();
        String code = StringUtil.randomAlphaNum(5);
        channel.setChannelCode(code);
        channel.setChannelUrl("http://jiebangbang/jie/?code=" + code);
        channel.setChannelName("test channel 1");
        channel.setCreator(1);
        channel.setReceiveMode(1);
        channelService.createChannal(channel);

        // 获取数据并验证
        Channel channel2 = channelService.getChannelByCode(code);
        assertEquals(channel.getChannelCode(), channel2.getChannelCode());
        assertEquals(channel.getChannelName(), channel2.getChannelName());
        assertEquals(channel.getChannelUrl(), channel2.getChannelUrl());
        assertEquals(channel.getCreator(), channel2.getCreator());

        // 插入第二条数据
        code = StringUtil.randomAlphaNum(5);
        channel.setChannelCode(code);
        channel.setChannelUrl("http://jiebangbang/jie/?code=" + code);
        channel.setChannelName("test channel 2");
        channel.setCreator(2);
        channelService.createChannal(channel);

        // 插入第三条条数据
        code = StringUtil.randomAlphaNum(5);
        channel.setChannelCode(code);
        channel.setChannelUrl("http://jiebangbang/jie/?code=" + code);
        channel.setChannelName("test channel 3");
        channel.setCreator(3);
        channelService.createChannal(channel);

        // 获取渠道列表， 返回3条数据
        List<Channel> channels = channelService.getChannels("test",1,DateUtil.getCurrentTimeStamp(),DateUtil.getCurrentTimeStamp());
        assertTrue(channels.size() == 3);

        // 更新数据
        String newName = "test channel 3 update";
        channel.setChannelName(newName);
        channel2 = channelService.getChannelByCode(code);
        assertEquals(channel.getCreator(), channel2.getCreator());

        // 删除最近一条插入的数据
        channelService.deleteChannel(code);

        // 再获取数据，应该返回长度为2
        channels = channelService.getChannels("test",1,DateUtil.getCurrentTimeStamp(),DateUtil.getCurrentTimeStamp());
        assertTrue(channels.size() == 2);
    }

    @Test
    public void getChannel() {
//        Channel channel = channelService.checkPhoneNumberAndPassword("123123", "11111111", "565GCa");
        List<ChannelStatistic> list = channelService.selectChannelStatisticS(null, 100002, null, null);
        Assert.assertTrue(list.size() > 1);
    }
}
