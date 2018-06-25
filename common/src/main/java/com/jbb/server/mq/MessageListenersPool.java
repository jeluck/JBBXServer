package com.jbb.server.mq;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.activemq.artemis.api.core.ActiveMQBuffer;
import org.apache.activemq.artemis.api.core.client.ClientMessage;
import org.apache.activemq.artemis.api.core.client.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.ExecutionException;
import com.jbb.server.shared.total.ListenerPool;

/**
 * MessageHandler implementation,
 * which invokes another class and run it in a separate thread received from a thread pool
 * @author VincentTang
 */
class MessageListenersPool implements MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageListenersPool.class);
    private static final BlockExecutionPolicy blockExecutionPolicy = new BlockExecutionPolicy();
    private static final long BUSY_POOL_DELAY = PropertyManager.getLongProperty("jbb.mq.busyPool.delay", 100L);

    private final MessageListener listener;
    private final ObjectListener objectListener;
    private final String listenerName;
    private final ThreadPoolExecutor pool;
    private final boolean blocking;
    private final boolean checkBusy;
    private final long busyPoolDelay;

    /**
     * Create a thread pool
     * @param listener Message Listener
     * @param threadPoolSize message listeners thread pool size: 
     *          if it is positive, a fixed thread pool will be used with the same size blocking queue,
     *          if it is zero or negative, an expandable thread pool will be used 
     */
    MessageListenersPool(AbstractListener listener, int threadPoolSize) {
        this.listener = listener instanceof MessageListener ? (MessageListener)listener : null;
        this.objectListener = listener instanceof ObjectListener ? (ObjectListener)listener : null;
        this.listenerName = listener.getClass().getName();

        if (threadPoolSize > 0) {
            int queueCapacity = this.objectListener != null ? Integer.MAX_VALUE : threadPoolSize << 1;
            pool = new ThreadPoolExecutor(threadPoolSize, threadPoolSize,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(queueCapacity));
            pool.setRejectedExecutionHandler(blockExecutionPolicy); // block execution, if the queue is full
            blocking = true;
            busyPoolDelay = BUSY_POOL_DELAY;
            checkBusy = busyPoolDelay > 0L;
        } else {
            pool = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());
            blocking = false;
            busyPoolDelay = 0L;
            checkBusy = false;
        }
    }
    
    @Override
    public void onMessage(ClientMessage message) {
        if (listener == null) {
            logger.error("Not message listener " + listenerName);
            return;
        }

        try {
            // read message
            ActiveMQBuffer buf = message.getBodyBuffer();
            byte[] messageBody = new byte[buf.readableBytes()];
            buf.readBytes(messageBody);

            message.individualAcknowledge();
            long expiration;

            if (blocking && (((expiration = message.getExpiration()) > 0) || checkBusy)) {
                if (checkBusy) {
                    final CountDownLatch startSignal = new CountDownLatch(1);

                    pool.execute(() -> {
                        startSignal.countDown();

                        if ((expiration > 0) && (expiration < System.currentTimeMillis())) return; // message expired

                        listener.onMessage(messageBody);
                    });

                    startSignal.await(busyPoolDelay, TimeUnit.MILLISECONDS);
                } else {
                    pool.execute(() -> {
                        if (expiration > System.currentTimeMillis()) {
                            listener.onMessage(messageBody);
                        }
                    });
                }
            } else {
                pool.execute(() -> listener.onMessage(messageBody));
            }
        } catch (Exception e) {
            logger.error("Exception in message processing in " + listenerName, e);
        }
    }

    void onObject(final Object object, final long expiration) {
        if (objectListener == null) {
            throw new ExecutionException("Not object listener " + listenerName);
        }

        if (blocking && (expiration > 0)) {
            pool.execute(() -> {
                if (expiration > System.currentTimeMillis()) {
                    objectListener.onObject(object);
                }
            });
        } else {
            pool.execute(() -> objectListener.onObject(object));
        }
    }
    
    /**
     * Shutdown the thread pool
     */
    void shutdown() {
        pool.shutdownNow();
    }
    
    ListenerPool getState() {
        return new ListenerPool(listenerName,
                pool.getActiveCount(), pool.getMaximumPoolSize(), pool.getPoolSize(),
                pool.getCompletedTaskCount(), getQueueSize(), ListenerPool.TYPE_MESSAGE);
    }

    private int getQueueSize() {
        BlockingQueue<Runnable> queue = pool.getQueue();
        return queue != null ? queue.size() : -1;
    }

    private static class BlockExecutionPolicy implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            BlockingQueue<Runnable> queue = executor.getQueue();
            if (queue != null) {
                try {
                    queue.put(r);
                } catch (InterruptedException ignore) {
                }
            }
        }
    }
}
