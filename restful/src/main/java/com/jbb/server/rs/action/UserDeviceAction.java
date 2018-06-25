package com.jbb.server.rs.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbb.server.core.domain.UserDevice;
import com.jbb.server.core.service.UserDeviceService;

@Service(UserDeviceAction.ACTION_NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserDeviceAction extends BasicAction {

    private static Logger logger = LoggerFactory.getLogger(UserDeviceAction.class);
    public static final String ACTION_NAME = "UserDeviceAction";

    @Autowired
    private UserDeviceService userDeviceService;

    public void saveUserDevice(Request req) {
        logger.debug(">saveUserDevice() deviceType = " + req.deviceType + " deviceToken = " + req.deviceToken
            + "installationId = " + req.installationId + "objectId = " + req.objectId);

        UserDevice userDevice = new UserDevice();
        userDevice.setDeviceToken(req.deviceToken);
        userDevice.setDeviceType(req.deviceType);
        userDevice.setInstallationId(req.installationId);
        userDevice.setUserId(this.user.getUserId());
        userDevice.setObjectId(req.objectId);
        userDevice.setStatus((req.status != null && req.status == true) ? true : false);
        userDeviceService.saveUserDevice(userDevice);

        logger.debug("<saveUserDevice()");
    }
    
    public void logoutDevice(String objectId){
        logger.debug(">logoutDevice objectId = " + objectId);

        userDeviceService.logoutUserDevice(this.user.getUserId(), objectId);

        logger.debug("<logoutDevice");
    }

    public static class Request {
        public String objectId;
        public String deviceToken;
        public String installationId;
        public String deviceType;
        public Boolean status;

    }
}
