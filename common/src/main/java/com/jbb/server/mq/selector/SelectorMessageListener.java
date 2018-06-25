package com.jbb.server.mq.selector;

import java.lang.reflect.Array;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.jbb.server.mq.MessageListener;
import com.jbb.server.shared.map.Mapper;

class SelectorMessageListener<T extends SelectorResponse> implements MessageListener, SelectorMessageCollector<T> {
    private final Class<T> objectType;
    private final ConcurrentHashMap<Integer, ObjectsHolder> holders = new ConcurrentHashMap<>();

    SelectorMessageListener(Class<T> objectType) {
        this.objectType = objectType;
    }

    @Override
    public void onMessage(byte[] message) {
        T object = Mapper.readObject(message, objectType);
        ObjectsHolder holder = holders.get(object.getSelector());
        if (holder != null) holder.addObject(object);
    }

    @Override
    public SelectorHolder<T> registerHolder(int selector, int maxMessages, long timeout, boolean iterating) {
        ObjectsHolder holder = new ObjectsHolder(selector, maxMessages, timeout, iterating);
        holders.put(selector, holder);
        return holder;
    }

    @Override
    public void clear(int selector) {
        holders.remove(selector);
    }

    private class ObjectsHolder implements SelectorHolder<T> {
        private final int selector;
        private volatile int ind;
        private final T[] objects;
        private CountDownLatch latch;
        private final boolean iterating;
        private int fetchInd;
        private final Object fetchLock;
        private volatile long timeout;

        @SuppressWarnings("unchecked")
        private ObjectsHolder(int selector, int size, long timeout, boolean iterating) {
            this.selector = selector;
            this.objects = (T[])Array.newInstance(objectType, size);
            this.timeout = timeout;
            this.iterating = iterating;

            if (timeout > 0) {
                if (iterating) {
                    this.fetchLock = new Object();
                    this.latch = new CountDownLatch(1);
                } else {
                    this.fetchLock = null;
                    this.latch = new CountDownLatch(size);
                }
            } else {
                this.fetchLock = null;
            }
        }

        private synchronized void addObject(T object) {
            if (ind < objects.length) {
                objects[ind] = object;

                if (timeout > 0) {
                    if (iterating) {
                        synchronized (fetchLock) {
                            ind++;
                            latch.countDown();
                        }
                    } else {
                        ind++;
                        latch.countDown();
                    }
                } else {
                    ind++;
                }
            }
        }

        @Override
        public T[] getAll() {
            if (timeout > 0) {
                try {
                    latch.await(timeout, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ignore) {
                }
            }

            holders.remove(selector);

            return ind == 0 ? null : objects;
        }

        @Override
        public T getOne() {
            if (timeout > 0) {
                long startTime = System.currentTimeMillis();
                try {
                    latch.await(timeout, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ignore) {
                }
                timeout -= System.currentTimeMillis() - startTime;
            }

            if (fetchInd >= ind) return null;

            int resInd = fetchInd;
            fetchInd++;

            if ((fetchInd == ind) && (timeout > 0)) {
                synchronized (fetchLock) {
                    if (fetchInd == ind) latch = new CountDownLatch(1);
                }
            }

            return objects[resInd];
        }

        @Override
        public int getSelector() {
            return selector;
        }
    }
}
