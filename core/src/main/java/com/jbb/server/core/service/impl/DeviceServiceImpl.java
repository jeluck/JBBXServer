package com.jbb.server.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.server.common.Constants;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.dao.DeviceDao;
import com.jbb.server.core.dao.UserEventLogDao;
import com.jbb.server.core.service.DeviceService;

@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private UserEventLogDao userEventLogDao;

    @Override
    public boolean saveDeviceSeqNumber(String seqNumber, String sourceId) {
        boolean flag = false;
        if (!deviceDao.checkDeviceExist(seqNumber)) {
            flag = deviceDao.insertDevice(seqNumber, DateUtil.getCurrentTimeStamp());
        }
        // 添加激活EventLog
        if (flag) {
            userEventLogDao.insertEventLog(null, sourceId, Constants.Event_LOG_EVENT_DEVICE_NAME,
                Constants.Event_LOG_EVENT_DEVICE_ACTION_ACTIVATE, null, null, null,  DateUtil.getCurrentTimeStamp());
        }
        return flag;
    }

}
