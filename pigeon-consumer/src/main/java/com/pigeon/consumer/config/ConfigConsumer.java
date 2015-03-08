package com.pigeon.consumer.config;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.pigeon.consumer.Consumer;
import com.pigeon.consumer.zookeeper.watcher.ConfigConsumerWatcher;
import com.pigeon.core.zookeeper.ZooKeeperConnector;

/**
 * 
 * <b><code>ConfigConsumer</code></b>
 * <p>
 * Comment here.
 * </p>
 * <b>Creation Time:</b> 2015-3-6
 * @author <a href="mailto:flysqrlboy@gmail.com">flysqrlboy</a>
 */
public class ConfigConsumer implements Consumer {

	private Logger logger = LoggerFactory.getLogger(ConfigConsumer.class);

	private ZooKeeperConnector connector;

	@Override
	public String consume(final String path) {
		Callable<byte[]> callable = new Callable<byte[]>() {
			public byte[] call() throws Exception {
				return connector.getClient().getData().usingWatcher(new ConfigConsumerWatcher(connector)).forPath(path);
			}
		};

		Retryer<byte[]> retryer = RetryerBuilder.<byte[]> newBuilder().retryIfException() // retry 条件
				.withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS)) //等待策略：固定重试间隔时间
				.withStopStrategy(StopStrategies.stopAfterAttempt(1)) //尝试次数
				.build();
		try {
			String result = ConfigHolder.get(path);
			if (result == null) {
				synchronized (path) {
					if (ConfigHolder.get(path) == null) {
						result = new String(retryer.call(callable), "UTF-8");
						ConfigHolder.put(path, result);
					}
				}
			}
			return result;
		} catch (RetryException e) {
			logger.error("Failed to get config from ZK for path=[{}]", path, e);
		} catch (ExecutionException e) {
			logger.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public void setConnector(ZooKeeperConnector connector) {
		this.connector = connector;
	}

}
