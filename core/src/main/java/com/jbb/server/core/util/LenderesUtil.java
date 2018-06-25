package com.jbb.server.core.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.util.DateUtil;
import com.jbb.server.core.dao.AccountDao;
import com.jbb.server.core.domain.User;

public class LenderesUtil {
    private static Logger logger = LoggerFactory.getLogger(LenderesUtil.class);

    public static final int CHANNEL_LENDER_ROLE_ID = 2;

    private static volatile LenderesUtil instance;
    private static final Object instanceLock = new Object();
    private static final long POLLING_INTERVAL = PropertyManager.getLongProperty("jbb.settings.refresh.lender", 180000);

    private AccountDao accountDao;
    private Timer timer;
    
    private Timer timerForSet;
    
    public static final Set<Integer> APPLY_USER_SET =Collections.synchronizedSet(new HashSet<Integer>());

    public static final ConcurrentHashMap<Integer, User> LENDERES = new ConcurrentHashMap<Integer, User>();

    private volatile boolean shutdownInProgress;

    private LenderesUtil() {
        try {
            accountDao = SpringUtil.getBean("AccountDao", AccountDao.class);
        } catch (Exception e) {
            logger.error("Exception in AccountDao init", e);
            return;
        }

        initTimer();

        logger.warn("LenderesUtil started");
    }

    public LenderesUtil(AccountDao accountDao) {
        if (accountDao == null) {
            logger.error("Null AccountDao");
            return;
        }
        this.accountDao = accountDao;

        initTimer();

        logger.warn("LenderesUtil(accountDao) started");
    }

    private void initTimer() {

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                loadLenderes();
            }
        }, 0, POLLING_INTERVAL);
        
        timerForSet=new Timer();
        //每隔三小时清理下用户申请列表
        timerForSet.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                APPLY_USER_SET.clear();
            }
        }, 0, 12 * DateUtil.HOUR_MILLSECONDES);
    }
    

    private void shutdown() {
        shutdownInProgress = true;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        
        if (timerForSet != null) {
            timerForSet.cancel();
            timerForSet = null;
        }
        logger.warn("LenderesUtil stopped");
    }

    public static LenderesUtil init() {
        synchronized (instanceLock) {
            if (instance == null) {
                logger.warn("New LenderesUtil");
                instance = new LenderesUtil();
            }
        }
        return instance;
    }

    public static LenderesUtil init(AccountDao accountDao) {
        synchronized (instanceLock) {
            if (instance == null) {
                logger.warn("New PropertiesUtil(AccountDao)");
                instance = new LenderesUtil(accountDao);
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

    private void loadLenderes() {
        try {
            List<User> lenders = accountDao.getUsers(null, CHANNEL_LENDER_ROLE_ID, true);
            if (lenders == null || lenders.size() == 0) {
                logger.info("refresh lenders , not found lenders ");
                return;
            } else {
                logger.info("refresh lenders , size = " + lenders.size());
                lenders.stream().forEach(user -> {
                    logger.info("lenders , user = " + user);
                    LENDERES.put(user.getUserId(), user);
                });

                LENDERES.forEach((userId, user)->{
                    logger.info("refreshed lenders , user = " + user);
                });
                
              
            }
        } catch (Exception e) {
            logger.error("Exception in loading lenderes", e);
        }
    }
}