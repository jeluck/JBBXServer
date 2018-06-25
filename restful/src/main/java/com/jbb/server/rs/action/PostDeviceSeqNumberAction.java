package com.jbb.server.rs.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbb.server.core.service.DeviceService;

/**
 * @Type LoginAction.java
 * @Desc
 * @author VincentTang
 * @date 2017年10月30日 下午6:08:16
 * @version
 */
@Service(PostDeviceSeqNumberAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PostDeviceSeqNumberAction extends BasicAction {

    public static final String ACTION_NAME = "PostDeviceSeqNumberAction";

    @Autowired
    private DeviceService deviceService;

    public boolean saveSeqNumber(String seqNumber, String sourceId) {

        return deviceService.saveDeviceSeqNumber(seqNumber, sourceId);
    }

}