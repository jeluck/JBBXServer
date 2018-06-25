package com.jbb.mgt.proc.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.mgt.proc.service.TelePhoneNumberProcessor;
import com.jbb.mgt.proc.service.UserRegisterH5Listener;
import com.jbb.mgt.server.core.util.OrgDataFlowSettingsUtil;
import com.jbb.mgt.server.core.util.PropertiesUtil;
import com.jbb.mgt.server.core.util.SpringUtil;
import com.jbb.server.common.Home;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.mq.MqClient;
import com.jbb.server.mq.Queues;

/**
 * Application startup and shutdown listener
 * 
 * @author Vincent Tang
 */
@WebListener
public class ApplicationEventListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationEventListener.class);

    /**
     * Application startup
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        logger.warn("Application started");
        Home.getHomeDir();
        Home.settingsTest();
        
        PropertiesUtil.init();
        OrgDataFlowSettingsUtil.init();

        //init TelePhoneNumberProcessor
        TelePhoneNumberProcessor.getInstance().initialize();
        
        //init process register event
        try {
            int poolSize, handlersNum;
            // user register pool
            poolSize = PropertyManager.getIntProperty("jbb.log.user.register.poolsize", 1);
            handlersNum = PropertyManager.getIntProperty("jbb.log.user.register.handlers", 1);
            MqClient.setMessageListenersPool(Queues.JBB_MGT_USER_REGISTER_QUEUE_ADDR, new UserRegisterH5Listener(), poolSize,
                handlersNum);
        } catch (Exception e) {
            logger.error("Exception in application initialisation", e);
        }

    }

    /**
     * Application shutdown
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        logger.warn("Destroying application...");

      
        //destroy TelePhoneNumberProcessor
        TelePhoneNumberProcessor.getInstance().destroy();
        
        
        OrgDataFlowSettingsUtil.destroy();
        PropertiesUtil.destroy();
        
        SpringUtil.close();
        Home.shutdown();

        logger.warn("Application destroyed");
    }

}
