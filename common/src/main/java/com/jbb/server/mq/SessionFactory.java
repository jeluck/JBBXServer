package com.jbb.server.mq;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.api.core.client.ActiveMQClient;
import org.apache.activemq.artemis.api.core.client.ClientSession;
import org.apache.activemq.artemis.api.core.client.ClientSessionFactory;
import org.apache.activemq.artemis.api.core.client.ServerLocator;
import org.apache.activemq.artemis.api.core.client.loadbalance.ConnectionLoadBalancingPolicy;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory;
import org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jbb.server.common.PropertyManager;
import com.jbb.server.common.util.StringUtil;

class SessionFactory {
    private static final Logger logger = LoggerFactory.getLogger(MqClient.class);

    private static ServerLocator locator;
    private static volatile ClientSessionFactory factory;
    private static final Object factoryLock = new Object();

    private static ClientSessionFactory getFactory() throws Exception {
        if (factory == null) {
            synchronized(factoryLock) {
                if (factory == null) {
                    // Server hosts - can contain empty values
                    String[] hosts = PropertyManager.getProperties("jbb.artemis.host");
                    if ((hosts == null) || (hosts.length == 0)) hosts = new String[1];
                    // Server ports
                    int[] ports = PropertyManager.getIntProperties("jbb.artemis.port", TransportConstants.DEFAULT_PORT);

                    TransportConfiguration[] servers = new TransportConfiguration[hosts.length];
                    StringBuilder logBuf = new StringBuilder();

                    for (int i = 0; i < hosts.length; i++) {
                        HashMap<String, Object> connectionParams = new HashMap<>(4);
                        if (!StringUtil.isEmpty(hosts[i])) connectionParams.put(TransportConstants.HOST_PROP_NAME, hosts[i]);

                        int port = (ports != null) && (ports.length > i) ? ports[i] : TransportConstants.DEFAULT_PORT;
                        connectionParams.put(TransportConstants.PORT_PROP_NAME, port);

                        logBuf.append("\n  host=").append(hosts[i]).append(", port=").append(port);

                        servers[i] = new TransportConfiguration(NettyConnectorFactory.class.getName(), connectionParams);
                    }

                    if (PropertyManager.getBooleanProperty("jbb.artemis.cluster", false) || (servers.length > 1)) {
                        // HA cluster
                        // Create a ServerLocator which will receive cluster topology updates from the cluster
                        // as servers leave or join and new backups are appointed or removed.
                        locator = ActiveMQClient.createServerLocatorWithHA(servers);

                        // The load balancing policy to be used by the client factory is configurable.
                        locator.setConnectionLoadBalancingPolicyClassName(FirstRoundRobinConnectionLoadBalancingPolicy.class.getName());

                        logger.warn("HA locator on" + logBuf.toString());
                    } else {
                        // Non-cluster
                        // Create a ServerLocator which creates session factories using a static list of transportConfigurations,
                        // the ServerLocator is not updated automatically as the cluster topology changes,
                        // and no HA backup information is propagated to the client
                        locator = ActiveMQClient.createServerLocatorWithoutHA(servers[0]);

                        logger.warn("Locator without HA on" + logBuf.toString());
                    }

                    // Client reconnection on server unavailability:
                    // This optional parameter determines the total number of reconnect attempts to make before giving up and shutting down.
                    // A value of -1 signifies an unlimited number of attempts.
                    // The default value is 0 - no attempts.
                    locator.setReconnectAttempts(-1);

                    // Session re-attachment replay buffer size.
                    // Other commands will be lost on session re-attachment or failover.
                    locator.setConfirmationWindowSize(PropertyManager.getIntProperty("jbb.artemis.ConfirmationWindowSize", 10485760));

                    // calls to send for durable messages on non transacted sessions will block
                    // until the message has reached the server, and a response has been sent back.
                    locator.setBlockOnDurableSend(true);
                    // Do not block calls to send for non-durable messages
                    locator.setBlockOnNonDurableSend(false);
                    // Do not block acknowledge calls to send for non transacted messages
                    locator.setBlockOnAcknowledge(false);

                    // message buffering on the client side, -1 is unlimited
                    int consumerWindowSize = PropertyManager.getIntProperty("jbb.artemis.ConsumerWindowSize", -2);
                    if (consumerWindowSize >= -1) locator.setConsumerWindowSize(consumerWindowSize);

                    locator.setCompressLargeMessage(true);
                    //locator.setMinLargeMessageSize(...); - use default value 100 KB

                    factory = locator.createSessionFactory();
                    factory.addFailoverListener(new FailoverListener());
                }
            }
        }

        return factory;
    }

    static ClientSession createSession() throws Exception {
        return getFactory().createSession();
    }

    static void close() {
        if (factory != null) {
            synchronized (factoryLock) {
                if (factory != null) {
                    factory.close();
                    locator.close();
                    factory = null;
                    locator = null;
                }
            }
        }
    }

    /**
     * With this policy the first node is 0'th, then each subsequent node is chosen sequentially in the same order.
     */
    public static final class FirstRoundRobinConnectionLoadBalancingPolicy implements ConnectionLoadBalancingPolicy, Serializable {
        private static final long serialVersionUID = 6511196010141439558L;
        private int pos;

        public int select(int max) {
            pos %= max;
            return pos++;
        }
    }
}
