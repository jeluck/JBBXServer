package com.jbb.server.common.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.jbb.server.common.exception.ExecutionException;

/**
 * Simple unlimited objects pool based on ConcurrentLinkedQueue.<br/>
 * Once borrowed the object always stays in memory until GC tries to remove it.
 * It should be used for pooling "cheap" local objects.<br/>
 * It provides two methods to borrow an object from the pool or create a new one, if the pool is empty,
 * and to return the object to the pool.
 * Class T must have a default constructor or factory must be provided.<br/>
 * Typical usage is next:<br/>
 * <code>
 * <br/>  try {
 * <br/>      obj = pool.getObject();
 * <br/>      ...
 * <br/>  } finally {
 * <br/>      pool.returnObject(obj);
 * <br/>  }
 * </code>
 * <br/><br/>
 * The call() method wraps this sequence.
 *
 * @author Dmitry Shirkalin
 */
public class SimpleObjectPool<T> implements ObjectPool<T> {
    /** Objects pool */
    private ConcurrentLinkedQueue<T> objectsPool = new ConcurrentLinkedQueue<>();
    private ObjectFactory<T> factory;
    
    public SimpleObjectPool(Class<T> clazz) {
        this.factory = () -> {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                throw new ExecutionException(e);
            }
        };
    }

    public SimpleObjectPool(ObjectFactory<T> factory) {
        this.factory = factory;
    }

    public SimpleObjectPool(int initial, ObjectFactory<T> factory) {
        this.factory = factory;
        populate(initial);
    }

    private void populate(int initial) {
        for (int i = 0; i < initial; i++) {
            objectsPool.offer(factory.create());
        }
    }

    /**
     * Borrow object from the pool or create a new one
     * @return object from the pool
     */
    @Override
    public T getObject() {
        T obj = objectsPool.poll();
        return obj != null ? obj : factory.create();
    }
    
    /**
     * Return the object to the pool
     * @param obj object
     */
    @Override
    public void returnObject(T obj) {
        if (obj != null) objectsPool.offer(obj);
    }
    
    /**
     * @return the pool size
     */
    @Override
    public int poolSize() {
        return objectsPool.size();
    }
}
