package com.jbb.server;

import static java.util.concurrent.TimeUnit.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class BeeperControl {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    private static ConcurrentHashMap<Integer, ScheduledFuture<?>> handers = new ConcurrentHashMap<>(100);

    public void sendUserWithDelay(int userId, long delay) {

        // 先查找是否已经加入，如果存在，则返回
        ScheduledFuture<?> pHander = handers.get(userId);
        if (pHander != null) {
            return;
        }

        final ScheduledFuture<?> hander = scheduler.schedule(new Runnable() {
            public void run() {
                System.out.println("beep" + userId);
                // beeperHandle.cancel(true);

            }
        }, delay, SECONDS);

        handers.put(userId, hander);
    }

    public void removeSendTask(int userId) {
        ScheduledFuture<?> hander = handers.get(userId);
        if (hander != null) {
            hander.cancel(true);
        }
        handers.remove(userId);
    }

    public void destory() {
        if (!scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}