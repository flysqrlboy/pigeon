package com.pigeon.consumer.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * <b><code>ConfigHolder</code></b>
 * <p>
 * conusmer从zookeeper服务端获取的配置缓存到本地内存
 * </p>
 * <b>Creation Time:</b> 2015-3-6
 * @author <a href="mailto:flysqrlboy@gmail.com">flysqrlboy</a>
 */
public class ConfigHolder {

	private static Map<String, String> configMap = new ConcurrentHashMap<String, String>();

	public static String get(String key) {
		return configMap.get(key);
	}

	public static void put(String key, String value) {
		configMap.put(key, value);
	}

	public static String remove(String key) {
		return configMap.remove(key);
	}
}
