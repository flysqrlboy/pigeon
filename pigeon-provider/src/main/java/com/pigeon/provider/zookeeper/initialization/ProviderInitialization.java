package com.pigeon.provider.zookeeper.initialization;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.pigeon.core.initialization.Initialization;
import com.pigeon.core.zookeeper.ZooKeeperConnector;
import com.pigeon.core.zookeeper.listener.PigeonConnectionStateListener;

public class ProviderInitialization extends Initialization {

	private static Logger logger = LoggerFactory.getLogger(ProviderInitialization.class);
	
	private CuratorFramework client;
	private ZooKeeperConnector connector;
	private long createWaitTime = 1000 * 3;
	
	@Override
	public void init() {
		this.client = connector.getClient();
		client.getConnectionStateListenable().addListener(new PigeonConnectionStateListener(this));
		reInit();
	}

	@Override
	public void reInit() {
		registerZNode(nodeName);
	}
	
	private void registerZNode(final String nodeName) {

		Callable<String> callable = new Callable<String>() {
			@Override
			public String call() throws Exception {
				try {
					return client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
							.forPath("/" + nodeName);
				} catch (NodeExistsException e) {
					logger.info("NodeExists node={},sleep {}ms", nodeName, createWaitTime);
					Thread.sleep(createWaitTime);
					return client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
							.forPath("/" + nodeName);
				} catch (Exception e) {
					throw e;
				}
			}
		};

		Retryer<String> retryer = RetryerBuilder.<String> newBuilder().retryIfException() // retry 条件
				.withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS)) //等待策略：固定重试间隔时间
				.withStopStrategy(StopStrategies.stopAfterAttempt(3)) //尝试次数
				.build();
		try {
			retryer.call(callable);
		} catch (Exception e) {
			logger.error("Failed to register for nodeId=" + nodeName, e);
			throw new RuntimeException(e);
		}
		logger.info("Success to register for nodeId=[{}]", nodeName);
	}

	public void setClient(CuratorFramework client) {
		this.client = client;
	}

	public void setConnector(ZooKeeperConnector connector) {
		this.connector = connector;
	}

	public void setCreateWaitTime(long createWaitTime) {
		this.createWaitTime = createWaitTime;
	}

}
