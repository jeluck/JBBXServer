package com.jbb.server.mq;

import com.jbb.server.common.PropertyManager;

/**
 * Queue names and addresses. If only one queue is linked to an address, it has the same name as the address. If a queue
 * or an address is single for all server instances, it is a constant, otherwise the server index and module name can be
 * added to the end. Some special queue names are read from system properties. !!! There is some limitation on a
 * valuable queue/address name length !!!
 */
public class Queues {
    public static final String SERVER_INDEX = PropertyManager.getProperty("jbb.server", "B1");
    private static final String CLUSTER = "cl."; // clustered addresses prefix

    // ****************************************************************
    // USER IO
    // ****************************************************************
    private static final String USER = "user.register."; // device address/queue prefix
    /** Device input data queue/address for processing */
    public static final String USER_REGISTER_QUEUE_ADDR = CLUSTER + USER + "in";
    
    
 // ****************************************************************
    // JBB MGT USER IO
    // ****************************************************************
    private static final String JBB_MGT_USER = "mgt.jbbuser.register."; // device address/queue prefix
    /** Device input data queue/address for processing */
    public static final String  JBB_MGT_USER_REGISTER_QUEUE_ADDR = CLUSTER + JBB_MGT_USER + "in";

}
