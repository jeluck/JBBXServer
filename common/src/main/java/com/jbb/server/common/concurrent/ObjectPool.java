package com.jbb.server.common.concurrent;

import java.util.function.Function;

/**
 * Unlimited objects pool<br/>
 * It provides two methods to borrow an object from the pool and to return the object to the pool.<br/>
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
public interface ObjectPool<T> {
    /**
     * Borrow object from the pool or create a new one
     * @return object from the pool
     */
    T getObject();

    /**
     * Return the object to the pool
     * @param obj object
     */
    void returnObject(T obj);
    
    /**
     * Borrow an object from the pool, call the function and return the object to the pool
     * @return the function result
     */
    default <R> R call(Function<T,R> function) {
        T obj = getObject();
        try {
            return function.apply(obj);
        } finally {
            returnObject(obj);
        }
    }

    int poolSize();
}
