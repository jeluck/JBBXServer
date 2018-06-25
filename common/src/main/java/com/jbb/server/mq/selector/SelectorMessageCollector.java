package com.jbb.server.mq.selector;

public interface SelectorMessageCollector<T extends SelectorResponse> {
    /**
     * Register a message object holder
     * @param selector unique selector value
     * @param maxMessages maximum number of messages to receive
     * @param timeout maximum wait timeout
     * @param iterating the holder can be either iterating and return object one by one or return all objects
     * @return the registered holder
     */
    SelectorHolder<T> registerHolder(int selector, int maxMessages, long timeout, boolean iterating);

    /**
     * Holder must be removed from the collector in the finally block by calling this method unless the getAll() method has been called.
     * @param selector selector value
     */
    void clear(int selector);
}
