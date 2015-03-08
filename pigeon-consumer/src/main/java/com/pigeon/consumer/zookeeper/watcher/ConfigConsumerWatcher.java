package com.pigeon.consumer.zookeeper.watcher;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.pigeon.consumer.config.ConfigHolder;
import com.pigeon.core.zookeeper.ZooKeeperConnector;

/**
 * 
 * <b><code>ConfigWatcher</code></b>
 * <p>
 * 配置变更监视器.
 * </p>
 * <b>Creation Time:</b> 2015-3-6
 * @author <a href="mailto:flysqrlboy@gmail.com">flysqrlboy</a>
 */
public class ConfigConsumerWatcher implements Watcher {

	private Logger logger = LoggerFactory.getLogger(ConfigConsumerWatcher.class);

	protected ZooKeeperConnector connector;

	protected CuratorFramework client;

	public ConfigConsumerWatcher(ZooKeeperConnector connector) {
		this.connector = connector;
		this.client = connector.getClient();
	}
	@Override
	public void process(final WatchedEvent event) {
		if (event.getType() == Event.EventType.NodeDataChanged) {

			String input = null;

			Callable<String> callable = new Callable<String>() {
				public String call() throws Exception {
					return new String(client.getData().usingWatcher(new ConfigConsumerWatcher(connector))
							.forPath(event.getPath()), "UTF-8");
				}
			};

			Retryer<String> retryer = RetryerBuilder.<String> newBuilder().retryIfException() // retry 条件
					.withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS)) //等待策略：固定重试间隔时间
					.withStopStrategy(StopStrategies.stopAfterAttempt(3)) //尝试次数
					.build();
			try {
				input = retryer.call(callable);
				if (input != null) {
					String oldValue = ConfigHolder.get(event.getPath());
					ConfigHolder.put(event.getPath(), input);
					if (oldValue != null) {
						logger.info("Config have been changed for path[{}], oldValue:[{}] -> newValue:[{}]",
								new String[] { event.getPath(), oldValue, input });
					} else {
						logger.info("Config have been added for path[{}], value:[{}]",
								new String[] { event.getPath(), oldValue, input });
					}
				}
			} catch (RetryException e) {
				logger.error("Failed to get config from ZK for path=[{}]", event.getPath());
			} catch (ExecutionException e) {
				logger.error(e.getMessage(), e);
			}

		} else if (event.getType() == Event.EventType.NodeDeleted) {
			logger.info("Config have been deleted for path[{}]", event.getPath());
			ConfigHolder.remove(event.getPath());
		}
	}

}
