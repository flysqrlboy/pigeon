package com.pigeon.consumer;

/**
 * 
 * <b><code>Consumer</code></b>
 * <p>
 * 把"向zookeeper服务端读取数据"抽象成provider
 * </p>
 * <b>Creation Time:</b> 2015-3-5
 * @author <a href="mailto:flysqrlboy@gmail.com">flysqrlboy</a>
 */
public interface Consumer {

	/**
	 * 
	 * 从zookeeper服务器的对应节点读取数据,key的名字就是ZNode节点的path
	 * 一个KV键值对，在zookeeper服务端以一个Znode节点的形式保存，key就是Znode path，
	 * value是 该节点的数据值
	 * @param path znode节点的路径
	 * @return
	 */
	public String consume(String key);
}
