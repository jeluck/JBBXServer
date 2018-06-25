package com.jbb.server.registerproc.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.common.Home;
import com.jbb.server.common.PropertyManager;
import com.jbb.server.core.util.LenderesUtil;
import com.jbb.server.core.util.PropertiesUtil;
import com.jbb.server.core.util.SpringUtil;
import com.jbb.server.mq.MqClient;
import com.jbb.server.mq.Queues;
import com.jbb.server.registerproc.service.UserRegisterH5Listener;

/**
 * Application startup and shutdown listener
 * 
 * @author Vincent Tang
 */
@WebListener
public class ApplicationEventListener implements ServletContextListener {

    private static Logger logger = LoggerFactory.getLogger(ApplicationEventListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Application started");
        PropertiesUtil.init();
        LenderesUtil.init();
        Home.getHomeDir();
        Home.settingsTest();

        try {
            int poolSize, handlersNum;
            // user register pool
            poolSize = PropertyManager.getIntProperty("jbb.log.user.register.poolsize", 5);
            handlersNum = PropertyManager.getIntProperty("jbb.log.user.register.handlers", 1);
            MqClient.setMessageListenersPool(Queues.USER_REGISTER_QUEUE_ADDR, new UserRegisterH5Listener(), poolSize,
                handlersNum);
        } catch (Exception e) {
            logger.error("Exception in application initialisation", e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        PropertiesUtil.destroy();
        LenderesUtil.destroy();
        SpringUtil.close();
        Home.shutdown();
        logger.info("Application destroyed");

    }

}
