package com.jbb.server.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.common.Home;

public class MqClientApp implements MessageListener, ObjectListener {
    private static Logger logger = LoggerFactory.getLogger(MqClientApp.class);

    private static final int TEST_COUNT = 10;

    private volatile int listenerCount = 0;
    private volatile long delay = 0;

    private synchronized int incrementCount() {
        return ++listenerCount;
    }

    public static void main(String[] args) {
        logger.info(">main()");
        System.out.println(Home.getHomeDir());

        try {
            testUserRegister();
        } catch (Exception e) {
            logger.error("Exception", e);
        } finally {
            Home.shutdown();
        }

        logger.info("<main()");
    }

    private static void testUserRegister() {
        logger.info(">testUserRegister()");

        try {
            String queue = "testUserRegister";
            MqClientApp listener = new MqClientApp();
            MqClient.setMessageListenersPool(queue, listener, TEST_COUNT / 3, 1);

            sendToListener(queue, "testUserRegister", 1, listener, false);

            System.out.println("Stop server and Press Enter");
            System.in.read();

            try {
                sendToListener(queue, "testUserRegister", 2, listener, false);
            } catch (Exception e) {
                logger.error("Exception after stopping", e);
            }

            System.out.println("Start server and Press Enter");
            System.in.read();

            sendToListener(queue, "testUserRegister", 3, listener, false);

            logger.info("testUserRegister: queueLength=" + MqClient.queueLength(queue));
        } catch (Exception e) {
            logger.error("Exception in testUserRegister", e);
        }

        logger.info("<testUserRegister()");
    }

    /**
     * Send a text message to a queue
     * 
     * @param queue queue name
     * @param name message prefix
     * @param ind message index
     * @param listener listener to log
     * @param checkQueueLength log queue length
     */
    private static void sendToListener(String queue, String name, int ind, MqClientApp listener,
        boolean checkQueueLength) {
        MqClient.send(queue, (name + ind).getBytes(), MqClient.NO_EXPIRY);
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info(name + ": sent=" + ind + ", received=" + listener.listenerCount + ", success="
            + (listener.listenerCount * 100 / ind) + "%"
            + (checkQueueLength ? ", queueLength=" + MqClient.queueLength(queue) : ""));
    }

    private static void sendObject(String queue, String name, int ind, MqClientApp listener, long expiry) {
        MqClient.sendObject(queue, name + ind, expiry);
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info(name + ": sent=" + ind + ", received=" + listener.listenerCount + ", success="
            + (listener.listenerCount * 100 / ind) + "%");
    }

    @Override
    public void onMessage(byte[] msg) {
        logger.info("onMessage() " + incrementCount() + ", received=" + new String(msg) + ", thread: "
            + Thread.currentThread().getName());

        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ignore) {
            }
        }
    }

    @Override
    public void onObject(Object object) {
        logger.info("onObject() " + incrementCount() + ", received=" + object + ", thread: "
            + Thread.currentThread().getName());

        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ignore) {
            }
        }
    }

}
