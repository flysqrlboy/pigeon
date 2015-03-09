package com.pigeon.provider;

/**
 * 
 * <b><code>Provider</code></b>
 * <p>
 * 把"向zookeeper服务端发送数据"抽象成provider 
 * </p>
 * <b>Creation Time:</b> 2015-3-5
 * @author <a href="mailto:flysqrlboy@gmail.com">flysqrlboy</a>
 */
public interface Provider {

	/**
	 * 
	 * 把数据发送到zookeeper服务器的对应节点.一个KV键值对，在zookeeper服务端以一个Znode节点的形式保存，key就是Znode path，
	 * value是 该节点的数据值
	 *
	 * @param key key对应一个znode节点
	 * @param value
	 */
	public void send(String key, String value);

}
