package com.jbb.server.mq.selector;

/**
 * Message object holder.
 * It can be either iterating and return object one by one or return all objects.
 * So either getOne() or getAll() can be called, but not both.
 */
public interface SelectorHolder<T> {
    T getOne();
    T[] getAll();
    int getSelector();
}
