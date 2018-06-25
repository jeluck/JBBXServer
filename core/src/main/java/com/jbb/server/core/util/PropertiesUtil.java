package com.jbb.server.core.util;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.jbb.server.common.PropertyManager;
import com.jbb.server.core.dao.SystemDao;
import com.jbb.server.core.domain.Property;

public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static volatile PropertiesUtil instance;
    private static final Object instanceLock = new Object();
    private static final long POLLING_INTERVAL = PropertyManager.getLongProperty("jbb.settings.refresh.db", 60000L);

    private SystemDao systemDao;
    private Timer timer;
    private int propsVersion = -1;

    private volatile boolean shutdownInProgress;

    private PropertiesUtil() {
        try {
            systemDao = SpringUtil.getBean("systemDao", SystemDao.class);
        } catch (Exception e) {
            logger.error("Exception in SystemDao init", e);
            return;
        }

        initTimer();

        logger.warn("PropertiesUtil started");
    }

    public PropertiesUtil(SystemDao systemDao) {
        if (systemDao == null) {
            logger.error("Null SystemDao");
            return;
        }
        this.systemDao = systemDao;

        initTimer();

        logger.warn("PropertiesUtil(SystemDao) started");
    }

    private void initTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                loadProperties();
            }
        }, 0, POLLING_INTERVAL);
    }

    private void shutdown() {
        shutdownInProgress = true;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        logger.warn("PropertiesUtil stopped");
    }

    public static PropertiesUtil init() {
        synchronized (instanceLock) {
            if (instance == null) {
                logger.warn("New PropertiesUtil");
                instance = new PropertiesUtil();
            }
        }
        return instance;
    }

    public static PropertiesUtil init(SystemDao systemDao) {
        synchronized (instanceLock) {
            if (instance == null) {
                logger.warn("New PropertiesUtil(SystemDao)");
                instance = new PropertiesUtil(systemDao);
            }
        }
        return instance;
    }

    public static void destroy() {
        synchronized (instanceLock) {
            if ((instance != null) && !instance.shutdownInProgress) {
                instance.shutdown();
                instance = null;
            }
        }
    }

    private void loadProperties() {
        try {
            int version = systemDao.getPropsVersion();
            if (propsVersion == version)
                return;

            // Since the version of properties has changed, load the properties from db:
            propsVersion = version;

            List<Property> props = systemDao.getSystemProperties();

            if (!CollectionUtils.isEmpty(props)) {
                if (logger.isDebugEnabled()) {
                    props.forEach(p -> {
                        String oldValue = PropertyManager.setProperty(p.getName(), p.getValue());

                        if (oldValue == null) {
                            logger.debug("New system property added: " + p.getName() + "=" + p.getValue());
                        } else if (!oldValue.equals(p.getValue())) {
                            logger.debug("Updated the system property " + p.getName() + ":\n  from: " + oldValue
                                + "\n    to: " + p.getValue());
                        }
                    });
                } else {
                    props.forEach(p -> PropertyManager.setProperty(p.getName(), p.getValue()));
                }
            }
        } catch (Exception e) {
            logger.error("Exception in loading properties", e);
        }
    }
}
