package com.pigeon.provider.config;

import java.io.UnsupportedEncodingException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigeon.core.zookeeper.ZooKeeperConnector;
import com.pigeon.provider.Provider;

/**
 * 
 * <b><code>ConfigProvider</code></b>
 * <p>
 * 把保存配置到zookeeper服务端的行为抽象成configProvider
 * </p>
 * <b>Creation Time:</b> 2015-3-5
 * @author <a href="mailto:flysqrlboy@gmail.com">flysqrlboy</a>
 */
public class ConfigProvider implements Provider {

	private Logger logger = LoggerFactory.getLogger(ConfigProvider.class);

	private ZooKeeperConnector connector;

	@Override
	public void send(String deliveryPath, String value) {
		try {
			Stat stat = connector.getClient().checkExists().forPath(deliveryPath);
			if (stat == null) {
				connector.getClient().create().withMode(CreateMode.PERSISTENT).forPath(deliveryPath);
			}
			connector.getClient().setData().inBackground().forPath(deliveryPath, value.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error("error in send data.[deliveryPath:{},value:{}]", new String[] { deliveryPath, value }, e);
		} catch (Exception e) {
			logger.error("error in send data.[deliveryPath:{},value:{}]", new String[] { deliveryPath, value }, e);
		}

	}

	public void setConnector(ZooKeeperConnector connector) {
		this.connector = connector;
	}

}
