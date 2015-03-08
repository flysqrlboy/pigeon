package com.pigeon.core.zookeeper;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <b><code>ZooKeeperConnector</code></b>
 * <p>
 * Comment here.
 * </p>
 * <b>Creation Time:</b> 2015-3-5
 * @author <a href="mailto:flysqrlboy@gmail.com">flysqrlboy</a>
 */
public class ZooKeeperConnector {

	private static Logger logger = LoggerFactory.getLogger(ZooKeeperConnector.class);

	/**
	 * like this 10.1.74.75:2281,10.1.74.75:2282,10.1.74.75:2283
	 */
	private String hosts;

	private String namespace;

	private CuratorFramework client;

	private static final int DEFAULT_SESSION_TIMEOUT_MS = 15 * 1000;
	private static final int DEFAULT_CONNECTION_TIMEOUT_MS = 10 * 1000;

	private int sessionTimeout = DEFAULT_SESSION_TIMEOUT_MS;
	private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT_MS;

	private static final String DEFAULT_NAMESPACE = "defaultConfig";

	public ZooKeeperConnector() {
		this(DEFAULT_NAMESPACE);
	}

	public ZooKeeperConnector(String namespace) {
		this.namespace = namespace;
	}
	public void connect() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		if (StringUtils.isEmpty(namespace)) {
			client = CuratorFrameworkFactory.newClient(hosts, sessionTimeout, connectionTimeout, retryPolicy);
		} else {
			client = CuratorFrameworkFactory.builder().connectString(hosts).sessionTimeoutMs(sessionTimeout)
					.connectionTimeoutMs(connectionTimeout).retryPolicy(retryPolicy).namespace(namespace).build();
		}
		client.start();
		logger.info("Successfully connected to Zookeeper [{}] ", hosts);
	}

	public void close() {
		CloseableUtils.closeQuietly(client);
	}

	public String getHosts() {
		return hosts;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
	}

	public CuratorFramework getClient() {
		if (client == null)
			connect();
		return client;
	}
	
	public CuratorFramework reconnect() {
		connect();
		return client;
	}

	public void setClient(CuratorFramework client) {
		this.client = client;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

}
