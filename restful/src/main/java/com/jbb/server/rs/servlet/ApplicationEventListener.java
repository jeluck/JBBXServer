package com.jbb.server.rs.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.core.util.ProcessUserOnStepOneUtil;
import com.jbb.server.core.util.SpringUtil;
import com.jbb.server.common.Home;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;

/**
 * Application startup and shutdown listener
 * 
 * @author Vincent Tang
 */
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
		
		ProcessUserOnStepOneUtil.init();
		
		//Aliyun短信平台 - 设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
	}

	/**
	 * Application shutdown
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.warn("Destroying application...");
		
		shutdownCleanupThread();

		SpringUtil.close();
		Home.shutdown();
		
		ProcessUserOnStepOneUtil.destroy();

		logger.warn("Application destroyed");
	}

	void shutdownCleanupThread() {
		try {
			AbandonedConnectionCleanupThread.shutdown();
		} catch (InterruptedException e) {
			logger.warn("SEVERE problem cleaning up: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
