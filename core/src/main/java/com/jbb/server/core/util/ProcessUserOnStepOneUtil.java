package com.jbb.server.core.util;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.mq.MqClient;
import com.jbb.server.mq.Queues;

public class ProcessUserOnStepOneUtil {
    private static Logger logger = LoggerFactory.getLogger(ProcessUserOnStepOneUtil.class);

    private static volatile ProcessUserOnStepOneUtil instance;
    private static final Object instanceLock = new Object();

    private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static ConcurrentHashMap<Integer, ScheduledFuture<?>> handers = new ConcurrentHashMap<>(100);

    private Timer timer;

    private volatile boolean shutdownInProgress;

    private ProcessUserOnStepOneUtil() {
        logger.warn("ProcessUserOnStepOneUtil started");
    }

    private void shutdown() {
        shutdownInProgress = true;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        logger.warn("ProcessUserOnStepOneUtil stopped");
    }

    public static ProcessUserOnStepOneUtil init() {
        synchronized (instanceLock) {
            if (instance == null) {
                logger.warn("New ProcessUserOnStepOneUtil");
                instance = new ProcessUserOnStepOneUtil();
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

    public static void sendUserWithDelay(int userId, long delay) {

        // 先查找是否已经加入，如果存在，则返回
        ScheduledFuture<?> pHander = handers.get(userId);
        if (pHander != null) {
            logger.debug("exist user, userId=" + userId);
            return;
        }

        final ScheduledFuture<?> hander = scheduler.schedule(new Runnable() {
            public void run() {
                // 发送注册用户消息至MQ
                logger.debug("send user to mq , userId=" + userId);
                MqClient.send(Queues.USER_REGISTER_QUEUE_ADDR, String.valueOf(userId).getBytes(), 0);
                handers.remove(userId);
            }
        }, delay, SECONDS);
        logger.debug("put user, userId=" + userId);
        handers.put(userId, hander);
    }

    public static void removeSendTask(int userId) {
        ScheduledFuture<?> hander = handers.get(userId);
        if (hander != null) {
            logger.debug("cancel send user , userId=" + userId);
            hander.cancel(false);
        }
        logger.debug("remove user , userId=" + userId);
        handers.remove(userId);
    }

}
