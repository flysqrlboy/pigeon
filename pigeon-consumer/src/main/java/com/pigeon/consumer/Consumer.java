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
	 * 从zookeeper服务器的对应节点读取数据
	 *
	 * @param path znode节点的路径
	 * @return
	 */
	public String consume(String path);
}
