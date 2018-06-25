package com.jbb.server.mq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.activemq.artemis.api.core.ActiveMQBuffer;
import org.apache.activemq.artemis.api.core.ActiveMQException;
import org.apache.activemq.artemis.api.core.ActiveMQInterruptedException;
import org.apache.activemq.artemis.api.core.ActiveMQNonExistentQueueException;
import org.apache.activemq.artemis.api.core.ActiveMQQueueExistsException;
import org.apache.activemq.artemis.api.core.SimpleString;
import org.apache.activemq.artemis.api.core.client.ClientConsumer;
import org.apache.activemq.artemis.api.core.client.ClientMessage;
import org.apache.activemq.artemis.api.core.client.ClientSession;
import org.apache.activemq.artemis.api.core.client.MessageHandler;
import org.apache.commons.pool2.ObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.exception.ExecutionException;
import com.jbb.server.shared.total.Listener;
import com.jbb.server.shared.total.ListenerPool;

public class MqClient {
    private static Logger logger = LoggerFactory.getLogger(MqClient.class);

    public static final byte HIGH_PRIORITY = 9;
    public static final byte LOW_PRIORITY = 0;
    public static final byte NO_PRIORITY = 5;
    public static final long NO_EXPIRY = 0L;

    private static final String SELECTOR = "X";
    private static final String SELECTOR_PREFIX = "x";
    private static final String SELECTOR_CONDITION = "='" + SELECTOR + "'";

    // extra time to add to message time to live in case of different time in boxes
    public static final long EXTRA_EXPIRY_TIME = PropertyManager.getLongProperty("jbb.mq.maxTimeDifference", 30L) * 1000L;

    private static final ConcurrentLinkedQueue<Consumer> consumers = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<Handler> handlers = new ConcurrentLinkedQueue<>();
    private static final ConcurrentHashMap<String, MessageListenersPool> objectListenersPool = new ConcurrentHashMap<>(16);

    /**
     * Send non-durable bytes message without priority and selector
     * @param address queues address, where to send the message
     *                When the message arrives on the server it is routed to any queues that are bound to the address.
     * @param messageBody message body
     * @param expiry message time to live in milliseconds since the current time; zero is unlimited
     */
    public static void send(String address, byte[] messageBody, long expiry) {
        send(address, messageBody, false, expiry, NO_PRIORITY, null);
    }

    /**
     * Send non-durable bytes message without selector
     * @param address queues address, where to send the message
     *                When the message arrives on the server it is routed to any queues that are bound to the address.
     * @param messageBody message body
     * @param expiry message time to live in milliseconds since the current time; zero is unlimited
     * @param priority priority value between 0 and 9
     */
    public static void send(String address, byte[] messageBody, long expiry, byte priority) {
        send(address, messageBody, false, expiry, priority, null);
    }

    /**
     * Send durable bytes message without selector, expiry and priority
     * @param address queues address, where to send the message
     *                When the message arrives on the server it is routed to any queues that are bound to the address.
     * @param messageBody message body
     */
    public static void sendDurable(String address, byte[] messageBody) {
        send(address, messageBody, true, NO_EXPIRY, NO_PRIORITY, null);
    }

    /**
     * Send bytes message
     * @param address queues address, where to send the message
     *                When the message arrives on the server it is routed to any queues that are bound to the address.
     * @param messageBody message body
     * @param durable durable messages lower performance, but in a durable queue will survive a server crash or restart
     * @param expiry message time to live in milliseconds since the current time; zero is unlimited
     * @param priority priority value between 0 and 9
     * @param selector optional selector filter property name
     */
    public static void send(String address, byte[] messageBody, boolean durable, long expiry, byte priority, String selector) {
        if (logger.isDebugEnabled()) {
            logger.debug(">send() address=" + address + ", size=" + messageBody.length +
                    ", expiry=" + expiry + ", selector=" + selector);
        }

        ObjectPool<Producer> pool = null;
        Producer producer = null;

        try {
            pool = PoolsFactory.getProducersPool(address);
            producer = pool.borrowObject();

            ClientMessage message = producer.getSession().createMessage(ClientMessage.BYTES_TYPE, durable);

            message.setPriority(priority);
            if (expiry > 0) message.setExpiration(System.currentTimeMillis() + expiry + EXTRA_EXPIRY_TIME);
            if (selector != null) message.putStringProperty(SELECTOR_PREFIX + selector, SELECTOR);

            message.getBodyBuffer().writeBytes(messageBody);

            producer.getProducer().send(message);
        } catch (ActiveMQInterruptedException ignore) { // Caused by: java.lang.InterruptedException
        } catch (Exception e) {
            throw new ExecutionException("Exception in sending message to " + address, e);
        } finally {
            // return session and message producer to the pool
            if ((pool != null) && (producer != null)) {
                try {
                    pool.returnObject(producer);
                } catch (Exception e) {
                    logger.warn("Exception returning producer: " + e.toString());
                }
            }
        }

        logger.debug("<send()");
    }

    /**
     * Create a queue, if it does not exists
     * @param name queue name
     * @param address an address, where the queue is bound
     * @param durable durable or non-durable queue
     */
    public static void createQueue(String name, String address, boolean durable) {
        logger.debug(">createQueue() name={}, address={}", name, address);

        ClientSession session = null;

        try {
            session = PoolsFactory.getSession();
            createQueue(session, name, address, durable);
        } catch (Exception e) {
            throw new ExecutionException("Exception in creating queue " + name +
                    " with address=" + address + ", durable=" + durable, e);
        } finally {
            try {
                PoolsFactory.returnSession(session);
            } catch (Exception e) {
                logger.error("Exception", e);
            }
        }

        logger.debug("<createQueue()");
    }

    private static void createQueue(ClientSession session, String name, String address, boolean durable) throws ActiveMQException {
        ClientSession.QueueQuery query = session.queueQuery(SimpleString.toSimpleString(name));
        if (query.isExists()) return;

        try {
            session.createQueue(address, name, durable);
            logger.debug("Queue {} created with address={}", name, address);
        } catch (ActiveMQQueueExistsException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Queue " + name + " exists: " + e.toString());
            }
        }
    }

    /**
     * Receive a message body from the queue
     * @param queue queue name
     * @param selector optional selector filter
     * @param timeout message waiting timeout in milliseconds; negative is unlimited; zero is no wait
     * @param address queue address to create it, if it does not exist, optional
     * @return message body or null, if the message not received
     */
    public static byte[] receive(String queue, String selector, long timeout, String address) {
        if (logger.isDebugEnabled()) {
            logger.debug(">receive() queue=" + queue + ", timeout=" + timeout + ", selector=" + selector);
        }

        ClientSession session = null;
        ClientConsumer consumer = null;
        byte[] messageBody = null;

        try {
            session = PoolsFactory.getSession();
            session.start();

            consumer = createConsumer(session, queue, selector, address);

            ClientMessage message = timeout == 0 ? consumer.receiveImmediate() :
                    timeout > 0 ? consumer.receive(timeout) : consumer.receive();

            if (message != null) {
                message.individualAcknowledge();

                ActiveMQBuffer buf = message.getBodyBuffer();
                messageBody = new byte[buf.readableBytes()];
                buf.readBytes(messageBody);
            }
        } catch (Exception e) {
            throw new ExecutionException("Exception in receiving message from " + queue, e);
        } finally {
            // close consumer
            if (consumer != null) {
                try {
                    consumer.close();
                } catch (Exception e) {
                    logger.warn("Exception in closing consumer: " + e.toString());
                }
            }

            // return session to the pool
            if (session != null) {
                try {
                    session.stop();
                    PoolsFactory.returnSession(session);
                } catch (Exception e) {
                    logger.warn("Exception in returning session: " + e.toString());
                }
            }
        }

        logger.debug("<receive()");
        return messageBody;
    }

    private static ClientConsumer createConsumer(ClientSession session, String queue, String selector)
    throws ActiveMQException {
        return selector == null ? session.createConsumer(queue) :
            session.createConsumer(queue, SELECTOR_PREFIX + selector + SELECTOR_CONDITION);
    }

    private static ClientConsumer createConsumer(ClientSession session, String queue, String selector, String address)
    throws ActiveMQException {
        try {
            return createConsumer(session, queue, selector);
        } catch (ActiveMQNonExistentQueueException e) {
            if (address == null) throw e;

            logger.warn("Queue " + queue + " does not exist, trying to recreate it on address=" + address + " : " + e.toString());
            createQueue(session, queue, address, false);
            return createConsumer(session, queue, selector);
        }
    }

    /**
     * Receive multiple messages from the queue
     * @param queue queue name
     * @param selector optional selector filter
     * @param timeout all messages waiting timeout in milliseconds; negative is unlimited; zero is no wait
     * @param address queue address to create it, if it does not exist, optional
     * @param maxMessagesNumber maximum messages number
     * @return a list of message bodies with the size less or equal to maxMessagesNumber
     */
    public static List<byte[]> receive(String queue, String selector, long timeout, String address, int maxMessagesNumber) {
        if (logger.isDebugEnabled()) {
            logger.debug(">receive(array) queue=" + queue + ", selector=" + selector +
                    ", timeout=" + timeout + ", maxMessagesNumber=" + maxMessagesNumber);
        }

        if (maxMessagesNumber <= 0) return null;

        long startTime = System.currentTimeMillis();

        ClientSession session = null;
        ClientConsumer consumer = null;
        ArrayList<byte[]> messageBodies = new ArrayList<>(maxMessagesNumber);

        try {
            session = PoolsFactory.getSession();
            session.start();

            consumer = createConsumer(session, queue, selector, address);

            for (int i = 0; i < maxMessagesNumber; i++) {
                ClientMessage message;

                if (timeout < 0) {
                    message = consumer.receive();
                } else {
                    long timeLeft = startTime + timeout - System.currentTimeMillis();
                    message = timeLeft > 0 ? consumer.receive(timeLeft) : consumer.receiveImmediate();
                }

                if (message == null) break;

                message.individualAcknowledge();

                ActiveMQBuffer buf = message.getBodyBuffer();
                byte[] messageBody = new byte[buf.readableBytes()];
                buf.readBytes(messageBody);

                messageBodies.add(messageBody);
            }
        } catch (Exception e) {
            throw new ExecutionException("Exception in receiving messages array from " + queue, e);
        } finally {
            // close consumer
            if (consumer != null) {
                try {
                    consumer.close();
                } catch (Exception e) {
                    logger.error("Exception in closing consumer: " + e.toString());
                }
            }

            // return session to the pool
            if (session != null) {
                try {
                    session.stop();
                    PoolsFactory.returnSession(session);
                } catch (Exception e) {
                    logger.warn("Exception in returning session: " + e.toString());
                }
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("<receive(array) size=" + messageBodies.size());
        }
        return messageBodies;
    }

    static long queueLength(String queue) {
        logger.debug(">queueLength() queue={}", queue);

        long len = 0;
        ClientSession session = null;

        try {
            session = PoolsFactory.getSession();
            ClientSession.QueueQuery query = session.queueQuery(SimpleString.toSimpleString(queue));

            if (query.isExists()) {
                len = query.getMessageCount();
            } else {
                logger.warn("Queue {} not exist, when count messages", queue);
            }
        } catch (Exception e) {
            logger.error("Exception in counting messages in " + queue, e);
        } finally {
            // return session to the pool
            if (session != null) {
                try {
                    PoolsFactory.returnSession(session);
                } catch (Exception e) {
                    logger.warn("Exception in returning session: " + e.toString());
                }
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("<queueLength() len=" + len);
        }
        return len;
    }

    public static void setMessageListener(String queue, String address, boolean durable, MessageListener listener, int handlersNum) {
        if (logger.isDebugEnabled()) {
            logger.debug(">setMessageListener() queue=" + queue + ", address=" + address +
                    ", durable=" + durable + ", listener=" + listener.getClass().getName());
        }

        BytesMessageHandler handler = new BytesMessageHandler(listener);

        int setCount = 0;
        if (handlersNum < 1) handlersNum = 1;

        for (int i = 0; i < handlersNum; i++) {
            Consumer consumer = setMessageHandler(queue, address, durable, handler, i == 0);
            if (consumer != null) {
                setCount++;

                if (listener instanceof ClosableConsumer) {
                    ((ClosableConsumer) listener).addConsumer(consumer);
                }
            }
        }

        if (setCount == 0) {
            logger.error("Listener for " + listener.getClass().getName() + " on queue=" + queue +
                " was not registered: address=" + address + ", durable=" + durable + ", handlersNum=" + handlersNum);
        } else {
            handlers.add(new Handler(handler, queue, address, setCount));

            logger.warn(listener.getClass().getSimpleName() + " started on queue " + queue +
                ", address=" + address + ", handlersNum=" + handlersNum);
        }

        logger.debug("<setMessageListener()");
    }

    private static Consumer setMessageHandler(String queue, String address, boolean durable, MessageHandler handler, boolean createQueue) {
        ClientSession session = null;
        ClientConsumer consumer = null;
        Consumer mqConsumer;

        try {
            session = SessionFactory.createSession();
            session.start();

            if (createQueue) createQueue(session, queue, address, durable);

            consumer = session.createConsumer(queue);
            consumer.setMessageHandler(handler);

            mqConsumer = new Consumer(session, consumer);
            consumers.add(mqConsumer);
        } catch (Exception e) {
            logger.error("Exception in setting a message handler: queue=" + queue + ", address=" + address +
                    ", durable=" + durable + ", handler=" + handler.getClass().getName(), e);
            try {
                if (consumer != null) consumer.close();
                if (session != null) session.close();
            } catch (Exception ee) {
                logger.warn("Exception in closing session: " + ee.toString());
            }

            mqConsumer = null;
        }

        return mqConsumer;
    }

    public static void setMessageListenersPool(String queue, String address, boolean durable, MessageListener listener, int threadPoolSize, int handlersNum) {
        if (logger.isDebugEnabled()) {
            logger.debug(">setMessageListenersPool() queue=" + queue + ", address=" + address + ", durable=" + durable +
                ", listener=" + listener.getClass().getName() + ", threadPoolSize=" + threadPoolSize + ", handlersNum=" + handlersNum);
        }

        MessageListenersPool listenersPool = new MessageListenersPool(listener, threadPoolSize);

        int setCount = 0;
        if (handlersNum < 1) handlersNum = 1;
        else if ((handlersNum > threadPoolSize) && (threadPoolSize > 0)) handlersNum = threadPoolSize;

        for (int i = 0; i < handlersNum; i++) {
            if (setMessageHandler(queue, address, durable, listenersPool, i == 0) != null) setCount++;
        }

        if (setCount == 0) {
            listenersPool.shutdown();

            logger.error("Listener pool for " + listener.getClass().getName() + " on queue=" + queue +
                " was not registered: address=" + address + ", durable=" + durable +
                ", threadPoolSize=" + threadPoolSize + ", handlersNum=" + handlersNum);
        } else {
            if (listener instanceof ObjectListener) {
                objectListenersPool.put(address, listenersPool);
            }
            handlers.add(new Handler(listenersPool, queue, address, setCount));

            logger.warn(listener.getClass().getSimpleName() + " started on queue=" + queue +
                ", address=" + address + ", poolSize=" + threadPoolSize + ", handlersNum=" + handlersNum);
        }

        logger.debug("<setMessageListenersPool()");
    }

    public static void setMessageListenersPool(String queueAddress, MessageListener listener, int threadPoolSize, int handlersNum) {
        setMessageListenersPool(queueAddress, queueAddress, false, listener, threadPoolSize, handlersNum);
    }

    public static void setObjectListenersPool(String address, ObjectListener listener, int threadPoolSize) {
        if (logger.isDebugEnabled()) {
            logger.debug(">setObjectListenersPool() address=" + address +
                ", listener=" + listener.getClass().getName() + ", threadPoolSize=" + threadPoolSize);
        }

        MessageListenersPool listenersPool = new MessageListenersPool(listener, threadPoolSize);
        objectListenersPool.put(address, listenersPool);
        handlers.add(new Handler(listenersPool, null, address, 1));

        logger.warn(listener.getClass().getSimpleName() + " started on address=" + address + ", poolSize=" + threadPoolSize);

        logger.debug("<setObjectListenersPool()");
    }

    public static void sendObject(String address, Object object, long expiry) {
        MessageListenersPool listenersPool = objectListenersPool.get(address);
        if (listenersPool != null) {
            if (expiry > 0) expiry += System.currentTimeMillis();
            listenersPool.onObject(object, expiry);
        } else {
            logger.error("Local listener pool not found for address=" + address + ", object " + object.getClass().getName());
        }
    }

    /**
     * Shutdown all listener pools and close all open consumers and MQ connections.
     * This method must be called on stopping the application!
     */
    public static synchronized void shutdown() {
        try {
            Consumer mqConsumer;
            while ((mqConsumer = consumers.poll()) != null) {
                mqConsumer.close();
            }
        } catch (Exception e) {
            logger.warn("Exception in closing stored consumers: " + e.toString());
        }

        Handler mqHandler;
        while ((mqHandler = handlers.poll()) != null) {
            MessageHandler handler = mqHandler.getMessageHandler();
            if (handler instanceof MessageListenersPool) {
                ((MessageListenersPool)handler).shutdown();
            }
        }

        PoolsFactory.shutdown();

        SessionFactory.close();
    }

    static void removeConsumer(Consumer consumer) {
        consumers.remove(consumer);
    }

    public static List<Listener> getListeners() {
        ArrayList<Listener> res = new ArrayList<>(handlers.size());
        HashMap<String, Listener> listeners = new HashMap<>(handlers.size());

        for (Handler mqHandler : handlers) {
            MessageHandler handler = mqHandler.getMessageHandler();

            if (handler instanceof MessageListenersPool) {
                ListenerPool pool = ((MessageListenersPool)handler).getState();
                pool.setQueue(mqHandler.getQueue());
                pool.setAddress(mqHandler.getAddress());
                pool.setConsumersCount(mqHandler.getConsumersCount());
                res.add(pool);
            } else if (handler instanceof BytesMessageHandler) {
                BytesMessageHandler byteHandler = (BytesMessageHandler)handler;
                String queue = mqHandler.getQueue();

                Listener listener = listeners.get(queue);
                if (listener == null) {
                    listener = new Listener(queue, mqHandler.getAddress(), byteHandler.getListenerName(), byteHandler.getTaskCount(),
                            mqHandler.getConsumersCount(), byteHandler.isRunning() ? 1 : 0, Listener.TYPE_MESSAGE);
                    listeners.put(queue, listener);
                    res.add(listener);
                } else {
                    listener.increment(byteHandler.getTaskCount(), byteHandler.isRunning() ? 1 : 0, mqHandler.getConsumersCount());
                }
            }
        }

        setQueueLength(res);

        return res;
    }

    private static void setQueueLength(List<Listener> listeners) {
        ClientSession session = null;

        try {
            session = PoolsFactory.getSession();

            for (Listener listener : listeners) {
                String queue = listener.getQueue();
                if (queue == null) continue;

                ClientSession.QueueQuery query = session.queueQuery(SimpleString.toSimpleString(queue));

                if (query.isExists()) {
                    listener.setQueueSize(query.getMessageCount());
                }
            }
        } catch (Exception e) {
            logger.error("Exception in counting messages", e);
        } finally {
            // return session to the pool
            if (session != null) {
                try {
                    PoolsFactory.returnSession(session);
                } catch (Exception e) {
                    logger.warn("Exception in returning session: " + e.toString());
                }
            }
        }
    }
}
