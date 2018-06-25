package com.jbb.server.common.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.jbb.server.shared.total.ListenerPool;

public class CancelTaskScheduler {
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
    
    private static class CancelTask implements Runnable {
        private Future<?> future;
        
        private CancelTask(Future<?> future) {
            this.future = future;
        }

        @Override
        public void run() {
            future.cancel(true);
        }
    }
    
    /**
     * Scheduler future task cancellation in specified timeout
     * @param future future task
     * @param delay timeout in milliseconds
     */
    public void scheduler(Future<?> future, long delay) {
        executor.schedule(new CancelTask(future), delay, TimeUnit.MILLISECONDS);
    }
    
    public void shutdown() {
        executor.shutdownNow();
    }
    
    @Override
    protected void finalize() {
        shutdown();
    }
    
    public ListenerPool getState() {
        return new ListenerPool(CancelTaskScheduler.class.getName(),
                executor.getActiveCount(), executor.getMaximumPoolSize(),
                executor.getPoolSize(), executor.getCompletedTaskCount(), getQueueSize(), ListenerPool.TYPE_SYSTEM);
    }

    private int getQueueSize() {
        BlockingQueue<Runnable> queue = executor.getQueue();
        return queue != null ? queue.size() : -1;
    }
}
