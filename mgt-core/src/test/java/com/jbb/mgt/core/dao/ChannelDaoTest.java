package com.jbb.mgt.core.dao;

import static org.junit.Assert.*;

import java.util.List;

import com.jbb.server.common.util.DateUtil;
import com.jbb.server.shared.rs.Util;
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

public class ChannelDaoTest {

    @Autowired
    private ChannelDao channelDao;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }

    @Test
    public void testChannelDao() {

        // 先删除所有测试数据
        List<Channel> all = channelDao.selectChannels(null,1, DateUtil.getCurrentTimeStamp(),DateUtil.getCurrentTimeStamp());
        for (Channel c : all) {
            channelDao.deleteChannel(c.getChannelCode());
        }
        
        // 先插入数据
        Channel channel = new Channel();
        String code = StringUtil.randomAlphaNum(5);
        channel.setChannelCode(code);
        channel.setChannelUrl("http://jiebangbang/jie/?code=" + code);
        channel.setChannelName("test channel 1");
        channel.setCreator(1);
        channelDao.insertChannal(channel);

        // 获取数据并验证
        Channel channel2 = channelDao.selectChannelByCode(code);
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
        channelDao.insertChannal(channel);

        // 插入第三条条数据
        code = StringUtil.randomAlphaNum(5);
        channel.setChannelCode(code);
        channel.setChannelUrl("http://jiebangbang/jie/?code=" + code);
        channel.setChannelName("test channel 3");
        channel.setCreator(3);
        channelDao.insertChannal(channel);

        // 获取渠道列表， 返回3条数据
        List<Channel> channels = channelDao.selectChannels(null,1,DateUtil.getCurrentTimeStamp(),DateUtil.getCurrentTimeStamp());
        assertTrue(channels.size() == 3);

        // 更新数据
        String newName = "test channel 3 update";
        channel.setChannelName(newName);
        channel2 = channelDao.selectChannelByCode(code);
        assertEquals(channel.getCreator(), channel2.getCreator());

        // 删除最近一条插入的数据
        channelDao.deleteChannel(code);

        // 再获取数据，应该返回长度为2
        channels = channelDao.selectChannels(null,1,DateUtil.getCurrentTimeStamp(),DateUtil.getCurrentTimeStamp());
        assertTrue(channels.size() == 2);
    }

}
